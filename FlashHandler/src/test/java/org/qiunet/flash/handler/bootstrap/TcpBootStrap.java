package org.qiunet.flash.handler.bootstrap;


import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.qiunet.flash.handler.bootstrap.hook.MyHook;
import org.qiunet.flash.handler.common.message.MessageContent;
import org.qiunet.flash.handler.context.session.DSession;
import org.qiunet.flash.handler.netty.client.param.TcpClientParams;
import org.qiunet.flash.handler.netty.client.tcp.NettyTcpClient;
import org.qiunet.flash.handler.netty.client.tcp.TcpClientConnector;
import org.qiunet.flash.handler.netty.client.trigger.IPersistConnResponseTrigger;
import org.qiunet.flash.handler.netty.server.BootstrapServer;
import org.qiunet.flash.handler.netty.server.hook.Hook;
import org.qiunet.flash.handler.netty.server.param.TcpBootstrapParams;
import org.qiunet.flash.handler.startup.context.StartupContext;
import org.qiunet.utils.scanner.ClassScanner;

import java.util.concurrent.locks.LockSupport;

/**
 * Created by qiunet.
 * 17/11/25
 */
public abstract class TcpBootStrap implements IPersistConnResponseTrigger {
	protected static final String host = "localhost";
	protected static final int port = 8888;
	protected static final Hook hook = new MyHook();
	protected TcpClientConnector tcpClientConnector;
	private static Thread currThread;

	@BeforeClass
	public static void init() throws Exception {
		ClassScanner.getInstance().scanner();

		currThread = Thread.currentThread();
		Thread thread = new Thread(() -> {
			TcpBootstrapParams tcpParams = TcpBootstrapParams.custom()
				.setStartupContext(new StartupContext())
				.setEncryption(true)
				.setPort(port)
				.build();

			BootstrapServer server = BootstrapServer.createBootstrap(hook).tcpListener(tcpParams);
			LockSupport.unpark(currThread);
			server.await();
		});
		thread.start();
		LockSupport.park();
	}
	@Before
	public void connect(){
		currThread = Thread.currentThread();
		NettyTcpClient tcpClient = NettyTcpClient.create(TcpClientParams.DEFAULT_PARAMS, this);
		tcpClientConnector = tcpClient.connect(host, port);
	}
	@After
	public void closeConnect(){
		currThread = Thread.currentThread();
		LockSupport.park();
	}

	@Override
	public void response(DSession session, MessageContent data) {
		this.responseTcpMessage(session, data);
		LockSupport.unpark(currThread);
	}

	protected abstract void responseTcpMessage(DSession session, MessageContent data);

	@AfterClass
	public static void shutdown() {
		BootstrapServer.sendHookMsg(hook.getHookPort(), hook.getShutdownMsg());
	}
}
