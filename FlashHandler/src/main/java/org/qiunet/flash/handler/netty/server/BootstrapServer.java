package org.qiunet.flash.handler.netty.server;

import io.netty.channel.ServerChannel;
import io.netty.util.CharsetUtil;
import org.qiunet.flash.handler.netty.param.HttpBootstrapParams;
import org.qiunet.flash.handler.netty.param.TcpBootstrapParams;
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
	private QLogger qLogger = LoggerManager.getLogger(LoggerType.FLASH_HANDLER);

	private volatile static BootstrapServer instance;

	private Thread httpThread;

	private Thread tcpThread;

	private NettyHttpServer httpServer;

	private NettyTcpServer tcpServer;

	private BootstrapServer(int shutdownPort, String shutdownMsg) {
		if (instance != null) throw new RuntimeException("Instance Duplication!");

		this.tcpServer = new NettyTcpServer();
		this.httpServer =  new NettyHttpServer();

		Thread thread = new Thread(new ShutdownListener(this , shutdownPort, shutdownMsg), "ShutdownListener");
		thread.setDaemon(true);
		thread.start();

		instance = this;
	}

	/***
	 * 创建一个bootstrap
	 * @param shutdownPort 停止的端口
	 * @param shutdownMsg 停止的消息
	 * @return
	 */
	public static BootstrapServer createBootstrap(int shutdownPort, String shutdownMsg) {
		if (StringUtil.isEmpty(shutdownMsg)) {
			throw new NullPointerException("shutdownMsg can not be empty!");
		}

		synchronized (BootstrapServer.class) {
			if (instance == null)
			{
				new BootstrapServer(shutdownPort, shutdownMsg);
			}
		}
		return instance;
	}


	public BootstrapServer httpListener(final HttpBootstrapParams params) {
		this.httpThread = new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					httpServer.start(params);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}, "BootstrapServer-Http");
		this.httpThread.setDaemon(true);
		this.httpThread.start();
		return this;
	}


	public BootstrapServer tcpListener(final TcpBootstrapParams params) {
		this.tcpThread = new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					tcpServer.start(params);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}, "BootstrapServer-Tcp");
		this.tcpThread.setDaemon(true);
		this.tcpThread.start();
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

		ShutdownListener(BootstrapServer bootstrapServer, int shutdownPort, String shutdownMsg) {
			this.shutdownMsg = shutdownMsg;
			this.server = bootstrapServer;

			try {
				ServerSocketChannel channel = ServerSocketChannel.open();
				channel.configureBlocking(false);
				channel.socket().bind(new InetSocketAddress(InetAddress.getByName("127.0.0.1"), shutdownPort));

				this.selector = Selector.open();
				channel.register(this.selector, SelectionKey.OP_ACCEPT);
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
								break;
							}
						}
					}
				} catch (IOException e) {
					e.printStackTrace();
				}

			}
		}
	}
}
