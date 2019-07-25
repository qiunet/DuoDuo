package org.qiunet.flash.handler.netty.server;

import io.netty.util.CharsetUtil;
import org.qiunet.flash.handler.netty.server.listener.ServerShutdownEventData;
import org.qiunet.flash.handler.netty.server.param.HttpBootstrapParams;
import org.qiunet.flash.handler.netty.server.param.TcpBootstrapParams;
import org.qiunet.flash.handler.netty.server.hook.Hook;
import org.qiunet.flash.handler.netty.server.http.NettyHttpServer;
import org.qiunet.flash.handler.netty.server.tcp.NettyTcpServer;
import org.qiunet.utils.listener.ListenerManager;
import org.qiunet.utils.listener.event_data.ServerStartEventData;
import org.qiunet.utils.logger.LoggerType;
import org.qiunet.utils.net.NetUtil;
import org.qiunet.utils.string.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
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
	private static final Logger logger = LoggerType.DUODUO.getLogger();

	private volatile static BootstrapServer instance;

	private Set<INettyServer> nettyServers = new HashSet<>(8);

	private HookListener hookListener;

	private Hook hook;
	private BootstrapServer(Hook hook) {
		if (instance != null) throw new RuntimeException("Instance Duplication!");
		if (hook == null) {
			throw new RuntimeException("hook can not be null");
		}
		this.hook = hook;
		hookListener = new HookListener(this , hook);
		Thread thread = new Thread(hookListener, "HookListener");
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
	 * 默认给本地的端口发送
	 * @param hookPort
	 * @param msg
	 */
	public static void sendHookMsg(int hookPort, String msg) {
		sendHookMsg("localhost", hookPort, msg);
	}

	/***
	 * 给服务器的钩子发送消息, 需要另起Main线程. 所以无法读取到之前的BootstrapServer .
	 * @param hookPort
	 * @param msg
	 */
	public static void sendHookMsg(String serverIp, int hookPort, String msg) {
		try {
			if (hookPort <= 0) {
				logger.error("BootstrapServer sendHookMsg but hookPort is less than 0!");
				System.exit(1);
			}
			logger.error("BootstrapServer sendHookMsg ["+msg+"]!");

			SocketChannel channel = SocketChannel.open(new InetSocketAddress(serverIp, hookPort));
			channel.write(ByteBuffer.wrap(msg.getBytes(CharsetUtil.UTF_8)));
			Thread.sleep(1000);
			channel.close();
		} catch (IOException e) {
			logger.error("BootstrapServer sendHookMsg: ", e);
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
		NettyHttpServer httpServer = new NettyHttpServer(params);
		this.nettyServers.add(httpServer);

		Thread httpThread = new Thread(httpServer, "BootstrapServer-Http Address ["+params.getAddress().toString()+"]");
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

		NettyTcpServer tcpServer = new NettyTcpServer(params);
		this.nettyServers.add(tcpServer);

		Thread tcpThread = new Thread(tcpServer, "BootstrapServer-Tcp Address ["+params.getAddress().toString()+"]");
		tcpThread.setDaemon(true);
		tcpThread.start();

		return this;
	}
	private Thread awaitThread;

	/***
	 * 阻塞线程 最后调用阻塞当前线程
	 */
	public void await(){
		ServerStartEventData.fireStartEventHandler();

		awaitThread = Thread.currentThread();
		LockSupport.park();
	}

	/**
	 * 通过shutdown 监听. 停止服务
	 */
	private void shutdown(){
		ListenerManager.fireEventHandler(new ServerShutdownEventData());

		if (hook != null) {
			hook.shutdown();
		}

		for (INettyServer server : this.nettyServers) {
			server.shutdown();
		}
		try {
			// 业务需要时间停止.
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		LockSupport.unpark(awaitThread);
	}

	/***
	 * Hook的监听
	 */
	private static class HookListener implements Runnable {
		private Hook hook;
		private Selector selector;
		private boolean RUNNING = true;
		private BootstrapServer server;
		private ServerSocketChannel serverSocketChannel;
		HookListener(BootstrapServer bootstrapServer, Hook hook) {
			this.hook = hook;
			this.server = bootstrapServer;

			try {
				serverSocketChannel = ServerSocketChannel.open();
				serverSocketChannel.configureBlocking(false);
				this.selector = Selector.open();

				serverSocketChannel.socket().bind(new InetSocketAddress(this.hook.getHookPort()));
				serverSocketChannel.register(this.selector, SelectionKey.OP_ACCEPT);
			} catch (IOException e) {
				this.RUNNING = false;
				server.shutdown();
				logger.error("[HookListener] Start Exception", e);
				Thread.currentThread().interrupt();
			}
		}
		/***
		 * 处理现有的消息. 可以用户自定义
		 * @param byteBuffer
		 * @throws IOException
		 */
		private boolean handlerMsg(ByteBuffer byteBuffer, SocketChannel channel) throws IOException {
			String msg = CharsetUtil.UTF_8.decode(byteBuffer).toString();
			String ip = ((InetSocketAddress)channel.getRemoteAddress()).getHostString();
			msg = StringUtil.powerfulTrim(msg);
			logger.error("[HookListener]服务端 Received Msg: ["+msg+"]");
			if (msg.equals(hook.getShutdownMsg())) {
				this.RUNNING = false;
				if (NetUtil.isLocalIp(ip)) {
					server.shutdown();
					return true;
				}else {
					logger.error("[HookListener]服务端 Shutdown but ip ["+ip+"] error");
					return false;
				}
			}else if (msg.equals(hook.getReloadCfgMsg())){
				hook.reloadCfg();
			}else {
				hook.custom(msg, ip);
			}
			return false;
		}

		@Override
		public void run() {
			logger.error("[HookListener]服务端 Hook Listener on port ["+hook.getHookPort()+"]");
			try {
			while (RUNNING) {
					try {
						this.selector.select(1000);
						Iterator<SelectionKey> itr = this.selector.selectedKeys().iterator();
						while (itr.hasNext()) {
							SelectionKey key = itr.next();
							itr.remove();

							if (key.isAcceptable()) {
								logger.error("[HookListener]服务端: ProcessAcceptor Msg");
								ServerSocketChannel serverSocketChannel = (ServerSocketChannel) key.channel();
								SocketChannel channel = serverSocketChannel.accept();

								String ip = ((InetSocketAddress)channel.getRemoteAddress()).getHostString();
								if (!NetUtil.isInnerIp(ip) && !NetUtil.isLocalIp(ip)) {
									logger.error("[HookListener]服务端: Remote ip ["+ip+"] is not allow !");
									channel.close();
									continue;
								}

								channel.configureBlocking(false);
								channel.register(this.selector, SelectionKey.OP_READ);
							}else if( key.isReadable()){
								SocketChannel channel = (SocketChannel) key.channel();
								ByteBuffer byteBuffer = ByteBuffer.allocate(2048);
								channel.read(byteBuffer);
								byteBuffer.flip();
								try {
									handlerMsg(byteBuffer, channel);
								}finally {
									channel.close();
								}
							}
						}
					}catch (Exception e) {
						logger.error("[HookListener]", e);
					}
				}
			}finally {
				try {
					this.selector.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
}
