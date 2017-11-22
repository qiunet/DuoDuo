package org.qiunet.flash.handler.netty.server;

import io.netty.util.CharsetUtil;
import org.qiunet.flash.handler.netty.param.HttpBootstrapParams;
import org.qiunet.flash.handler.netty.param.TcpBootstrapParams;
import org.qiunet.flash.handler.netty.server.hook.ShutdownHook;
import org.qiunet.flash.handler.netty.server.http.NettyHttpServer;
import org.qiunet.flash.handler.netty.server.tcp.NettyTcpServer;
import org.qiunet.utils.logger.LoggerManager;
import org.qiunet.utils.logger.LoggerType;
import org.qiunet.utils.logger.log.QLogger;
import org.qiunet.utils.string.StringUtil;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
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

	private ShutdownHook hook;
	private BootstrapServer(int shutdownPort, String shutdownMsg, ShutdownHook hook) {
		if (instance != null) throw new RuntimeException("Instance Duplication!");

		this.hook = hook;

		Thread thread = new Thread(new ShutdownListener(this , shutdownPort, shutdownMsg), "ShutdownListener");
		thread.setDaemon(true);
		thread.start();

		instance = this;
	}
	/***
	 * 可以自己添加给hook.
	 * @param shutdownPort
	 * @param shutdownMsg
	 * @param hook  钩子 关闭时候,先执行你的代码
	 * @return
	 */
	public static BootstrapServer createBootstrap(int shutdownPort, String shutdownMsg, ShutdownHook hook) {
		if (StringUtil.isEmpty(shutdownMsg)) {
			throw new NullPointerException("shutdownMsg can not be empty!");
		}

		synchronized (BootstrapServer.class) {
			if (instance == null)
			{
				new BootstrapServer(shutdownPort, shutdownMsg, hook);
			}
		}
		return instance;
	}
	/***
	 * 创建一个bootstrap
	 * @param shutdownPort 停止的端口
	 * @param shutdownMsg 停止的消息
	 * @return
	 */
	public static BootstrapServer createBootstrap(int shutdownPort, String shutdownMsg) {
		return createBootstrap(shutdownPort, shutdownMsg, null);
	}

	/***
	 * 停止服务器, 需要另起Main线程. 所以无法读取到之前的BootstrapServer .
	 * @param shutdownPort
	 * @param shutdownMsg
	 */
	public static void sendShutdown(int shutdownPort, String shutdownMsg) {
		try {
			if (shutdownPort <= 0) {
				qLogger.error("BootstrapServer sendShutdown but shutdownPort is less than 0!");
				System.exit(1);
			}
			qLogger.error("BootstrapServer sendShutdown And stopServer!");

			SocketChannel channel = SocketChannel.open(new InetSocketAddress(InetAddress.getByName("localhost"), shutdownPort));
			channel.write(ByteBuffer.wrap(shutdownMsg.getBytes(CharsetUtil.UTF_8)));
			channel.close();

		} catch (IOException e) {
			qLogger.error("BootstrapServer sendShutdown: ", e);
			System.exit(1);
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
		LockSupport.unpark(awaitThread);
	}

	/***
	 * 停止的监听
	 */
	private static class ShutdownListener implements Runnable {
		private QLogger qLogger = LoggerManager.getLogger(LoggerType.FLASH_HANDLER);

		private Selector selector;
		private String shutdownMsg;
		private BootstrapServer server;
		private ServerSocketChannel serverSocketChannel;
				ShutdownListener(BootstrapServer bootstrapServer, int shutdownPort, String shutdownMsg) {
			this.shutdownMsg = shutdownMsg;
			this.server = bootstrapServer;

			try {
				serverSocketChannel = ServerSocketChannel.open();
				serverSocketChannel.configureBlocking(false);
				serverSocketChannel.socket().bind(new InetSocketAddress(InetAddress.getByName("127.0.0.1"), shutdownPort));

				this.selector = Selector.open();
				serverSocketChannel.register(this.selector, SelectionKey.OP_ACCEPT);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		@Override
		public void run() {
			qLogger.error("[shutdownListener]服务端: 启动成功");
			while (true) {
				try {
					this.selector.select();
					Iterator<SelectionKey> itr = this.selector.selectedKeys().iterator();
					while (itr.hasNext()) {
						SelectionKey key = itr.next();
						itr.remove();

						if (key.isAcceptable()) {
							qLogger.error("[shutdownListener]服务端: Acceptor Msg");
							ServerSocketChannel serverSocketChannel = (ServerSocketChannel) key.channel();
							SocketChannel channel = serverSocketChannel.accept();
							channel.configureBlocking(false);

							channel.register(this.selector, SelectionKey.OP_READ);
						}else if( key.isReadable()){
							SocketChannel channel = (SocketChannel) key.channel();
							ByteBuffer byteBuffer = ByteBuffer.allocate(1000);
							channel.read(byteBuffer);
							byteBuffer.flip();
							String msg = CharsetUtil.UTF_8.decode(byteBuffer).toString();
							msg = StringUtil.powerfulTrim(msg);
							qLogger.error("[shutdownListener]服务端 Received Msg: ["+msg+"]");
							if (msg.equals(shutdownMsg)) {

								// 释放锁.
								server.shutdown();
								// 不调整位置, shutdown 后关闭. 脚本能看到结果. 否则关闭后. 线程其实还在shutdown
								channel.close();
								this.serverSocketChannel.close();
								this.selector.close();
								break;
							}
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
				}

			}
		}
	}
}
