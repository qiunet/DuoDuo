package org.qiunet.game.tests.server;


import org.qiunet.flash.handler.context.header.ProtocolHeaderType;
import org.qiunet.flash.handler.netty.server.BootstrapServer;
import org.qiunet.flash.handler.netty.server.hook.Hook;
import org.qiunet.flash.handler.netty.server.param.TcpBootstrapParams;
import org.qiunet.game.tests.server.context.StartupContext;
import org.qiunet.game.tests.server.enums.ServerType;
import org.qiunet.utils.logger.LoggerType;
import org.slf4j.Logger;

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
				server.tcpListener(TcpBootstrapParams.custom()
						.setProtocolHeaderType(ProtocolHeaderType.server)
						.setStartupContext(new StartupContext())
						.setPort(ServerType.LC_ROOM.port())
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
		private final Logger logger = LoggerType.DUODUO_GAME_TEST.getLogger();

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
		public void custom(String msg) {

		}
	}
}
