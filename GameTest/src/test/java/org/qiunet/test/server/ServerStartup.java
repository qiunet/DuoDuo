package org.qiunet.test.server;


import org.qiunet.flash.handler.netty.server.BootstrapServer;
import org.qiunet.flash.handler.netty.server.hook.Hook;
import org.qiunet.flash.handler.netty.server.param.HttpBootstrapParams;
import org.qiunet.flash.handler.netty.server.param.TcpBootstrapParams;
import org.qiunet.test.server.error.ErrorHandler;
import org.qiunet.test.server.interceptor.TestLogicInterceptor;
import org.qiunet.test.server.interceptor.TestOnlineInterceptor;
import org.qiunet.test.server.interceptor.TestRoomInterceptor;
import org.qiunet.test.server.type.ServerType;
import org.qiunet.utils.logger.LoggerType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.locks.LockSupport;

/**
 * 测试时候启动服务
 * Created by qiunet.
 * 17/12/9
 */
public final class ServerStartup {
	private static final MyHook hook = new MyHook();
	private boolean startuped;
	public void startup() {
		if (this.startuped) throw new IllegalStateException("server was startup.");
		this.startuped = true;

		final Thread currThread = Thread.currentThread();
		Thread thread = new Thread(new Runnable() {
			@Override
			public void run() {
				BootstrapServer server = BootstrapServer.createBootstrap(hook);
				server.httpListener(HttpBootstrapParams.custom()
						.setHttpInterceptor(new TestLogicInterceptor())
						.setPort(ServerType.HTTP_LOGIC.uri().getPort())
						.setWebSocketInterceptor(new TestOnlineInterceptor())
						.setErrorMessage(new ErrorHandler())
						.setWebsocketPath("/ws")
						.build());

				server.tcpListener(TcpBootstrapParams.custom()
						.setPort(ServerType.LC_ROOM.uri().getPort())
						.setTcpInterceptor(new TestRoomInterceptor())
						.setErrorMessage(new ErrorHandler())
						.build());
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				LockSupport.unpark(currThread);
				server.await();
			}
		}, "Client-Server-Startup");
		thread.setDaemon(true);
		thread.start();
		LockSupport.park();
	}

	public void shutdown(){
		if (!startuped) throw new IllegalStateException("Server was shutdown .");
		BootstrapServer.sendHookMsg(hook.getHookPort(), hook.getShutdownMsg());
	}
	/***
	 * 自己的钩子
	 */
	private static class MyHook implements Hook {
		private Logger logger = LoggerType.DUODUO.getLogger();

		@Override
		public String getReloadCfgMsg() {
			return "Reload";
		}

		@Override
		public void reloadCfg() {

		}

		@Override
		public int getHookPort() {
			return 1314;
		}

		@Override
		public String getShutdownMsg() {
			return "Qiu-Yang-Shutdown";
		}

		@Override
		public void shutdown() {
			logger.error("Called shutdown");
		}

		@Override
		public void custom(String msg, String ip) {

		}
	}
}
