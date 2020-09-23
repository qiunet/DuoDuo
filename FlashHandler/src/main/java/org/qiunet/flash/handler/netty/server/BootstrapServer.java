package org.qiunet.flash.handler.netty.server;

import io.netty.util.CharsetUtil;
import org.qiunet.flash.handler.netty.server.hook.Hook;
import org.qiunet.flash.handler.netty.server.http.NettyHttpServer;
import org.qiunet.flash.handler.netty.server.param.HttpBootstrapParams;
import org.qiunet.flash.handler.netty.server.param.TcpBootstrapParams;
import org.qiunet.flash.handler.netty.server.tcp.NettyTcpServer;
import org.qiunet.listener.event.data.ServerShutdownEventData;
import org.qiunet.listener.event.data.ServerStartupEventData;
import org.qiunet.utils.logger.LoggerType;
import org.qiunet.utils.net.NetUtil;
import org.qiunet.utils.secret.StrCodecUtil;
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
	private static final String ICON = "0a2e2e2e2e2e2e2e2e2e2e2e2e2e2e2e2e2e2e2e2e2e2ee998bfe5bca5e99980e4bd9b2e2e" +
		"2e2e2e2e2e2e2e2e2e2e2e2e2e2e2e2e2e2e2e2e0a20202020202020202020202020202020202020202020205f6f6f306f6f5f202" +
		"020202020202020202020202020202020202020200a202020202020202020202020202020202020202020206f383838383838386f" +
		"2020202020202020202020202020202020202020200a20202020202020202020202020202020202020202020383822202e2022383" +
		"82020202020202020202020202020202020202020200a20202020202020202020202020202020202020202020287c202d5f2d207c" +
		"292020202020202020202020202020202020202020200a20202020202020202020202020202020202020202020305c20203d20202" +
		"f302020202020202020202020202020202020202020200a202020202020202020202020202020202020205f5f5f2fe280982d2d2d" +
		"e280995c5f5f5f202020202020202020202020202020202020200a2020202020202020202020202020202020202e27205c7c20202" +
		"0202020207c2f20272e20202020202020202020202020202020200a20202020202020202020202020202020202f205c5c7c7c7c20" +
		"203a20207c7c7c2f2f205c202020202020202020202020202020200a202020202020202020202020202020202f205f7c7c7c7c7c2" +
		"02de58d8d2d7c7c7c7c7c5f205c2020202020202020202020202020200a2020202020202020202020202020207c2020207c205c5c" +
		"5c20202d20202f2f2f207c2020207c20202020202020202020202020200a2020202020202020202020202020207c205c5f7c20202" +
		"7275c2d2d2d2f272720207c5f2f207c20202020202020202020202020200a2020202020202020202020202020205c20202e2d5c5f" +
		"5f2020272d2720205f5f5f2f2d2e202f20202020202020202020202020200a202020202020202020202020205f5f5f272e202e272" +
		"0202f2d2d2e2d2d5c2020272e202e275f5f5f2020202020202020202020200a2020202020202020202e222220e280983c2020e280" +
		"982e5f5f5f5c5f3c7c3e5f2f5f5f5f2ee280993ee280992022222e202020202020202020200a202020202020207c207c203a2020e" +
		"280982d205ce280982e3be280985c205f202fe280993b2ee280992f202d20e28099203a207c207c20202020202020200a20202020" +
		"20202020205c20205c20e280985f2e2020205c5f205f5f5c202f5f5f205f2f2020202e2de28099202f20202f20202020202020200" +
		"a202020203d3d3d3d3de280982d2e5f5f5f5fe280982e5f5f5f205c5f5f5f5f5f2f5f5f5f2e2de280995f5f5f2e2de280993d3d3d" +
		"3d3d20202020200a2020202020202020202020202020202020202020202020e280983d2d2d2d3de28099202020202020202020202" +
		"020202020202020202020200a20202020202020202020202020202020202020202020202020202020202020202020202020202020" +
		"2020202020202020202020200a2e2e2e2e2e2e2e2e2e2e2e2e2e2e2e2e2e2e2e2ee4bd9be7a596e4bf9de4bd91202ce6b0b8e697a" +
		"04255472e2e2e2e2e2e2e2e2e2e2e2e2e2e2e2e2e2e2e";

	private static final Logger logger = LoggerType.DUODUO_FLASH_HANDLER.getLogger();
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
		logger.info(StrCodecUtil.decrypt(ICON));
		ServerStartupEventData.fireStartupEventHandler();

		awaitThread = Thread.currentThread();
		LockSupport.park();
	}

	/**
	 * 通过shutdown 监听. 停止服务
	 */
	private void shutdown(){
		ServerShutdownEventData.fireShutdownEventHandler();

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

				serverSocketChannel.socket().bind(new InetSocketAddress(InetAddress.getByName("127.0.0.1"), this.hook.getHookPort()));
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
		private boolean handlerMsg(ByteBuffer byteBuffer) {
			String msg = CharsetUtil.UTF_8.decode(byteBuffer).toString();
			msg = StringUtil.powerfulTrim(msg);
			logger.error("[HookListener]服务端 Received Msg: ["+msg+"]");
			if (msg.equals(hook.getShutdownMsg())) {
				this.RUNNING = false;
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
