package org.qiunet.flash.handler.netty.server;

import io.netty.util.CharsetUtil;
import org.qiunet.flash.handler.netty.server.param.HttpBootstrapParams;
import org.qiunet.flash.handler.netty.server.param.TcpBootstrapParams;
import org.qiunet.flash.handler.netty.server.hook.Hook;
import org.qiunet.flash.handler.netty.server.http.NettyHttpServer;
import org.qiunet.flash.handler.netty.server.tcp.NettyTcpServer;
import org.qiunet.utils.logger.LoggerManager;
import org.qiunet.utils.logger.LoggerType;
import org.qiunet.utils.logger.log.QLogger;
import org.qiunet.utils.nonSyncQuene.factory.DefaultThreadFactory;
import org.qiunet.utils.string.StringUtil;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.concurrent.Future;
import java.util.concurrent.locks.LockSupport;

/**
 * Created by qiunet.
 * 17/11/21
 */
public class BootstrapServer {
	private static final QLogger qLogger = LoggerManager.getLogger(LoggerType.FLASH_HANDLER);

	private volatile static BootstrapServer instance;

	private NettyHttpServer httpServer;

	private NettyTcpServer tcpServer;

	private Hook hook;
	private BootstrapServer(Hook hook) {
		if (instance != null) throw new RuntimeException("Instance Duplication!");
		if (hook == null) {
			throw new RuntimeException("hook can not be null");
		}
		this.hook = hook;

		Thread thread = new Thread(new HookListener(this , hook), "HookListener");
		thread.start();

		instance = this;
	}
	/***
	 * 可以自己添加给hook.
	 * @param hook  钩子 关闭时候,先执行你的代码
	 * @return
	 */
	public static BootstrapServer createBootstrap(Hook hook) {
		if (StringUtil.isEmpty(hook.getShutdownMsg())) {
			throw new NullPointerException("shutdownMsg can not be empty!");
		}

		synchronized (BootstrapServer.class) {
			if (instance == null)
			{
				new BootstrapServer(hook);
			}
		}
		return instance;
	}

	/***
	 * 给服务器的钩子发送消息, 需要另起Main线程. 所以无法读取到之前的BootstrapServer .
	 * @param hookPort
	 * @param msg
	 */
	public static void sendHookMsg(int hookPort, String msg) {
		try {
			if (hookPort <= 0) {
				qLogger.error("BootstrapServer sendHookMsg but hookPort is less than 0!");
				System.exit(1);
			}
			qLogger.error("BootstrapServer sendHookMsg ["+msg+"]!");

			SocketChannel channel = SocketChannel.open(new InetSocketAddress(InetAddress.getByName("localhost"), hookPort));
			channel.write(ByteBuffer.wrap(msg.getBytes(CharsetUtil.UTF_8)));
			Thread.sleep(1000);
			channel.close();
		} catch (IOException e) {
			qLogger.error("BootstrapServer sendHookMsg: ", e);
			System.exit(1);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 启动http监听
	 * @param params
	 * @return
	 */
	public BootstrapServer httpListener(HttpBootstrapParams params) {
		if (this.httpServer != null) {
			throw new RuntimeException("httpServer already setting! ");
		}
		this.httpServer = new NettyHttpServer(params);
		Thread httpThread = new Thread(this.httpServer, "BootstrapServer-Http");
		httpThread.setDaemon(true);
		httpThread.start();
		return this;
	}

	/**
	 * 启动tcp监听
	 * @param params
	 * @return
	 */
	public BootstrapServer tcpListener(TcpBootstrapParams params) {
		if (this.tcpServer != null) {
			throw new RuntimeException("tcpServer already setting! ");
		}

		this.tcpServer = new NettyTcpServer(params);
		Thread tcpThread = new Thread(tcpServer, "BootstrapServer-Tcp");
		tcpThread.setDaemon(true);
		tcpThread.start();
		return this;
	}
	public static Thread awaitThread;

	/***
	 * 阻塞线程 最后调用阻塞当前线程
	 */
	public void await(){
		awaitThread = Thread.currentThread();
		LockSupport.park();
	}

	/**
	 * 通过shutdown 监听. 停止服务
	 */
	private void shutdown(){
		if (hook != null) {
			hook.shutdown();
		}
		if (this.httpServer != null) {
			this.httpServer.shutdown();
		}
		if (this.tcpServer != null) {
			this.tcpServer.shutdown();
		}
		LockSupport.unpark(awaitThread);
	}

	/***
	 * Hook的监听
	 */
	private static class HookListener implements Runnable {
		private QLogger qLogger = LoggerManager.getLogger(LoggerType.FLASH_HANDLER);
		private Hook hook;
		private BootstrapServer server;
		private AsynchronousChannelGroup hookListenerGroup;
		private AsynchronousServerSocketChannel serverChannel;
		HookListener(BootstrapServer bootstrapServer, Hook hook) {
			this.hook = hook;
			this.server = bootstrapServer;
			try {
				this.hookListenerGroup = AsynchronousChannelGroup.withFixedThreadPool(1, new DefaultThreadFactory("Hook-Listener-Asynchronous-Channel-Group"));
				serverChannel = AsynchronousServerSocketChannel.open(hookListenerGroup);
				serverChannel.bind(new InetSocketAddress("localhost", hook.getHookPort()));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		@Override
		public void run() {
			qLogger.error("[HookListener]服务端: 启动成功");
			this.serverChannel.accept(null, new CompletionHandler<AsynchronousSocketChannel, Void>() {
				@Override
				public void completed(AsynchronousSocketChannel result, Void attachment) {
					serverChannel.accept(null, this);

					ByteBuffer byteBuffer = ByteBuffer.allocate(1000);
					Future<Integer> future = result.read(byteBuffer);
					try {
						if (future.get() > 0) this.handlerMsg((ByteBuffer) byteBuffer.flip());
					} catch (Exception e) {
						qLogger.error("[HookListener]处理消息异常: ", e);
					}finally {
						try {
							result.close();
						} catch (IOException e) {
							qLogger.error("[HookListener]关闭异常: ", e);
						}
					}
				}
				/***
				 * 处理现有的消息. 可以用户自定义
				 * @param byteBuffer
				 * @throws IOException
				 */
				private boolean handlerMsg(ByteBuffer byteBuffer) throws IOException {
					String msg = CharsetUtil.UTF_8.decode(byteBuffer).toString();
					msg = StringUtil.powerfulTrim(msg);
					qLogger.error("[HookListener]服务端 Received Msg: ["+msg+"]");
					if (msg.equals(hook.getShutdownMsg())) {
						server.shutdown();
						hookListenerGroup.shutdown();
						return true;
					}else if (msg.equals(hook.getReloadCfgMsg())){
						hook.reloadCfg();
					}else {
						hook.custom(msg);
					}
					return false;
				}
				@Override
				public void failed(Throwable exc, Void attachment) {
					qLogger.error("[HookListener]异常: ", exc);
				}
			});
		}
	}
}
