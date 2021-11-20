package org.qiunet.flash.handler.netty.server;

import io.netty.util.CharsetUtil;
import org.qiunet.flash.handler.netty.server.hook.Hook;
import org.qiunet.flash.handler.netty.server.http.NettyHttpServer;
import org.qiunet.flash.handler.netty.server.param.HttpBootstrapParams;
import org.qiunet.flash.handler.netty.server.param.TcpBootstrapParams;
import org.qiunet.flash.handler.netty.server.tcp.NettyTcpServer;
import org.qiunet.utils.exceptions.CustomException;
import org.qiunet.utils.listener.event.data.ServerShutdownEventData;
import org.qiunet.utils.listener.event.data.ServerStartupEventData;
import org.qiunet.utils.logger.LoggerType;
import org.qiunet.utils.net.NetUtil;
import org.qiunet.utils.string.StringUtil;
import org.slf4j.Logger;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.locks.LockSupport;

/**
 * Created by qiunet.
 * 17/11/21
 */
public class BootstrapServer {
	private static final Logger logger = LoggerType.DUODUO_FLASH_HANDLER.getLogger();
	private static BootstrapServer instance;

	private final Set<INettyServer> nettyServers = new HashSet<>(8);
	private final HookListener hookListener;

	private BootstrapServer(Hook hook) {
		if (instance != null) throw new CustomException("Instance Duplication!");
		if (hook == null) {
			throw new CustomException("hook can not be null");
		}
		hookListener = new HookListener(hook);
	}
	/***
	 * 可以自己添加给hook.
	 * @param hook  钩子 关闭时候,先执行你的代码
	 * @return 实例
	 */
	public static BootstrapServer createBootstrap(Hook hook) {
		if (StringUtil.isEmpty(hook.getShutdownMsg())) {
			throw new NullPointerException("shutdownMsg can not be empty!");
		}

		synchronized (BootstrapServer.class) {
			if (instance == null)
			{
				instance = new BootstrapServer(hook);
			}
		}
		return instance;
	}

	/***
	 * 给服务器的钩子发送消息, 需要另起Main线程. 所以无法读取到之前的BootstrapServer .
	 * 默认给本地的端口发送
	 * @param hookPort 钩子端口
	 * @param msg 发送消息内容
	 */
	public static void sendHookMsg(int hookPort, String msg) {
		try (DatagramChannel channel = DatagramChannel.open()){
			if (hookPort <= 0) {
				logger.error("BootstrapServer sendHookMsg but hookPort is less than 0!");
				System.exit(1);
			}
			logger.error("BootstrapServer sendHookMsg [{}]!", msg);

			channel.send(ByteBuffer.wrap(msg.getBytes(CharsetUtil.UTF_8)), new InetSocketAddress("localhost", hookPort));
		} catch (IOException e) {
			logger.error("BootstrapServer sendHookMsg: ", e);
			System.exit(1);
		}
	}

	/**
	 * 启动http监听
	 * @param params 启动http的参数
	 * @return server实例
	 */
	public BootstrapServer httpListener(HttpBootstrapParams params) {
		NettyHttpServer httpServer = new NettyHttpServer(params);
		this.nettyServers.add(httpServer);
		return this;
	}
	/**
	 * 启动tcp监听
	 * @param params Tcp 启动参数
	 * @return server实例
	 */
	public BootstrapServer tcpListener(TcpBootstrapParams params) {

		NettyTcpServer tcpServer = new NettyTcpServer(params);
		this.nettyServers.add(tcpServer);
		return this;
	}

	private Thread awaitThread;
	/***
	 * 阻塞线程 最后调用阻塞当前线程
	 */
	public void await(){
		Thread hookThread = new Thread(hookListener, "HookListener");
		hookThread.setDaemon(true);
		hookThread.start();
		try {
			ServerStartupEventData.fireStartupEventHandler();
		}catch (CustomException e) {
			e.logger(logger);
			System.exit(1);
		} catch(Exception e) {
			logger.error(e.getMessage(), e);
			System.exit(1);
		}

		for (INettyServer nettyServer : nettyServers) {
			Thread thread = new Thread(nettyServer, nettyServer.threadName());
			thread.setDaemon(true);
			thread.start();
		}
		awaitThread = Thread.currentThread();
		LockSupport.park();
	}

	/***
	 * Hook的监听
	 */
	private class HookListener implements Runnable {
		private final ByteBuffer buffer = ByteBuffer.allocate(256);
		private boolean running = true;
		private final Hook hook;
		HookListener(Hook hook) {
			this.hook = hook;
		}

		/**
		 * 通过shutdown 监听. 停止服务
		 */
		private void shutdown(){
			ServerShutdownEventData.fireShutdownEventHandler();

			if (hook != null) {
				hook.shutdown();
			}

			for (INettyServer server : nettyServers) {
				server.shutdown();
			}

			LockSupport.unpark(awaitThread);
		}

		/***
		 * 处理现有的消息. 可以用户自定义
		 * @param channel channel
		 */
		private void handlerMsg(DatagramChannel channel) {
			buffer.clear();
			try {
				channel.receive(buffer);
			} catch (IOException e) {
				e.printStackTrace();
			}
			buffer.flip();
			String msg = CharsetUtil.UTF_8.decode(buffer).toString();
			msg = StringUtil.powerfulTrim(msg);
			logger.error("[HookListener]服务端 Received Msg: [{}]", msg);
			if (msg.equals(hook.getShutdownMsg())) {
				this.running = false;
				this.shutdown();
			}else if (msg.equals(hook.getReloadCfgMsg())){
				hook.reloadCfg();
			}else {
				hook.custom(msg);
			}
		}

		@Override
		public void run() {
			logger.error("[HookListener]服务端 Hook Listener on udp port [{}]", hook.getHookPort());
			try(DatagramChannel channel = DatagramChannel.open()) {
				try {
					channel.bind(new InetSocketAddress(InetAddress.getByName("127.0.0.1"), this.hook.getHookPort()));
				}catch (Exception e) {
					logger.error("Bind error", e);
					System.exit(1);
				}

				while (running) {
					this.handlerMsg(channel);
				}

			} catch (IOException e) {
				logger.error("[HookListener]", e);
				System.exit(1);
			}
		}
	}
}
