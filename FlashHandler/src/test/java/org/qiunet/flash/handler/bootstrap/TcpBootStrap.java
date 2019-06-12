package org.qiunet.flash.handler.bootstrap;


import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.qiunet.flash.handler.bootstrap.error.DefaultErrorMessage;
import org.qiunet.flash.handler.bootstrap.hook.MyHook;
import org.qiunet.flash.handler.common.message.MessageContent;
import org.qiunet.flash.handler.handler.mapping.RequestHandlerScanner;
import org.qiunet.flash.handler.interceptor.DefaultTcpInterceptor;
import org.qiunet.flash.handler.netty.client.param.TcpClientParams;
import org.qiunet.flash.handler.netty.client.trigger.ILongConnResponseTrigger;
import org.qiunet.flash.handler.netty.client.tcp.NettyTcpClient;
import org.qiunet.flash.handler.netty.server.BootstrapServer;
import org.qiunet.flash.handler.netty.server.hook.Hook;
import org.qiunet.flash.handler.netty.server.param.TcpBootstrapParams;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.util.concurrent.locks.LockSupport;

/**
 * Created by qiunet.
 * 17/11/25
 */
public abstract class TcpBootStrap extends RequestHandlerScanner implements ILongConnResponseTrigger {
	protected static String host = "localhost";
	protected static int port = 8888;
	protected static Hook hook = new MyHook();
	protected NettyTcpClient tcpClient;
	private static Thread currThread;
	@BeforeClass
	public static void init(){
		currThread = Thread.currentThread();
		Thread thread = new Thread(() -> {
			TcpBootstrapParams tcpParams = TcpBootstrapParams.custom()
					.setTcpInterceptor(new DefaultTcpInterceptor())
					.setErrorMessage(new DefaultErrorMessage())
					.setPort(port)
					.setEncryption(true)
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
		try {
			tcpClient = new NettyTcpClient(TcpClientParams.custom()
				.setAddress(new InetSocketAddress(InetAddress.getByName(host), port))
				.build(), this);
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
	}
	@After
	public void closeConnect(){
		currThread = Thread.currentThread();
		LockSupport.park();
		tcpClient.close();
	}

	@Override
	public void response(MessageContent data) {
		this.responseTcpMessage(data);
		LockSupport.unpark(currThread);
	}

	protected abstract void responseTcpMessage(MessageContent data);

	@AfterClass
	public static void shutdown() {
		BootstrapServer.sendHookMsg(hook.getHookPort(), hook.getShutdownMsg());
	}
}
