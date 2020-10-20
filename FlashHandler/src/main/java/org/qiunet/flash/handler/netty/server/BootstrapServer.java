package org.qiunet.flash.handler.netty.server;

import io.netty.util.CharsetUtil;
import org.qiunet.flash.handler.netty.server.hook.Hook;
import org.qiunet.flash.handler.netty.server.http.NettyHttpServer;
import org.qiunet.flash.handler.netty.server.param.HttpBootstrapParams;
import org.qiunet.flash.handler.netty.server.param.TcpBootstrapParams;
import org.qiunet.flash.handler.netty.server.tcp.NettyTcpServer;
import org.qiunet.listener.event.data.ServerShutdownEventData;
import org.qiunet.listener.event.data.ServerStartupEventData;
import org.qiunet.utils.exceptions.CustomException;
import org.qiunet.utils.logger.LoggerType;
import org.qiunet.utils.net.NetUtil;
import org.qiunet.utils.string.StringUtil;
import org.slf4j.Logger;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
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

	private Set<INettyServer> nettyServers = new HashSet<>(8);

	private Hook hook;
	private BootstrapServer(Hook hook) {
		if (instance != null) throw new CustomException("Instance Duplication!");
		if (hook == null) {
			throw new CustomException("hook can not be null");
		}
		this.hook = hook;
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
		sendHookMsg("localhost", hookPort, msg);
	}

	/***
	 * 给服务器的钩子发送消息, 需要另起Main线程. 所以无法读取到之前的BootstrapServer .
	 * @param hookPort 钩子端口
	 * @param msg 发送消息内容
	 */
	public static void sendHookMsg(String serverIp, int hookPort, String msg) {
		try (SocketChannel channel = SocketChannel.open(new InetSocketAddress(serverIp, hookPort))){
			if (hookPort <= 0) {
				logger.error("BootstrapServer sendHookMsg but hookPort is less than 0!");
				System.exit(1);
			}
			logger.error("BootstrapServer sendHookMsg [{}]!", msg);

			channel.write(ByteBuffer.wrap(msg.getBytes(CharsetUtil.UTF_8)));
		} catch (IOException e) {
			logger.error("BootstrapServer sendHookMsg: ", e);
			System.exit(1);
		}
	}

	/**
	 * 启动http监听
	 * @param params 启动http的参数
	 * @return
	 */
	public BootstrapServer httpListener(HttpBootstrapParams params) {
		NettyHttpServer httpServer = new NettyHttpServer(params);
		this.nettyServers.add(httpServer);
		return this;
	}
	/**
	 * 启动tcp监听
	 * @param params Tcp 启动参数
	 * @return
	 */
	public BootstrapServer tcpListener(TcpBootstrapParams params) {

		NettyTcpServer tcpServer = new NettyTcpServer(params);
		this.nettyServers.add(tcpServer);
		return this;
	}
	private HookListener hookListener;
	private Thread awaitThread;
	/***
	 * 阻塞线程 最后调用阻塞当前线程
	 */
	public void await(){
		hookListener = new HookListener(this, hook);
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

	/**
	 * 通过shutdown 监听. 停止服务
	 */
	private void shutdown(){
		ServerShutdownEventData.fireShutdownEventHandler();
		try {
			hookListener.serverSocketChannel.close();
		} catch (IOException e) {}

		if (hook != null) {
			hook.shutdown();
		}

		for (INettyServer server : this.nettyServers) {
			server.shutdown();
		}

		LockSupport.unpark(awaitThread);
	}

	/***
	 * Hook的监听
	 */
	private static class HookListener implements Runnable {
		private Hook hook;
		private Selector selector;
		private boolean running = true;
		private BootstrapServer server;
		private ServerSocketChannel serverSocketChannel;
		HookListener(BootstrapServer bootstrapServer, Hook hook) {
			this.hook = hook;
			this.server = bootstrapServer;

			try {
				serverSocketChannel = ServerSocketChannel.open();
				serverSocketChannel.configureBlocking(false);
				this.selector = Selector.open();

				serverSocketChannel.socket().bind(new InetSocketAddress(InetAddress.getByName("127.0.0.1"), this.hook.getHookPort()));
				serverSocketChannel.register(this.selector, SelectionKey.OP_ACCEPT);
			} catch (IOException e) {
				this.running = false;
				server.shutdown();
				throw new CustomException(e, "Start up hook listener error!");
			}
		}
		/***
		 * 处理现有的消息. 可以用户自定义
		 * @param byteBuffer 消息buffer
		 */
		private boolean handlerMsg(ByteBuffer byteBuffer) {
			String msg = CharsetUtil.UTF_8.decode(byteBuffer).toString();
			msg = StringUtil.powerfulTrim(msg);
			logger.error("[HookListener]服务端 Received Msg: [{}]", msg);
			if (msg.equals(hook.getShutdownMsg())) {
				this.running = false;
				server.shutdown();
				return true;
			}else if (msg.equals(hook.getReloadCfgMsg())){
				hook.reloadCfg();
			}else {
				hook.custom(msg);
			}
			return false;
		}

		@Override
		public void run() {
			logger.error("[HookListener]服务端 Hook Listener on port [{}]", hook.getHookPort());
			try {
			while (running) {
					try {
						this.selector.select(1000);
						Iterator<SelectionKey> itr = this.selector.selectedKeys().iterator();
						while (itr.hasNext()) {
							SelectionKey key = itr.next();
							itr.remove();

							if (key.isAcceptable()) {
								logger.error("[HookListener]服务端: ProcessAcceptor Msg");
								SocketChannel channel = ((ServerSocketChannel) key.channel()).accept();

								String ip = ((InetSocketAddress)channel.getRemoteAddress()).getHostString();
								if (!NetUtil.isInnerIp(ip) && !NetUtil.isLocalIp(ip)) {
									logger.error("[HookListener]服务端: Remote ip [{}] is not allow !", ip);
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
									handlerMsg(byteBuffer);
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
