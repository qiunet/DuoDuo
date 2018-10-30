package org.qiunet.flash.handler.bootstrap;


import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.qiunet.flash.handler.bootstrap.error.DefaultErrorMessage;
import org.qiunet.flash.handler.bootstrap.hook.MyHook;
import org.qiunet.flash.handler.common.message.MessageContent;
import org.qiunet.flash.handler.context.session.DefaultSessionEvent;
import org.qiunet.flash.handler.handler.mapping.RequestHandlerScanner;
import org.qiunet.flash.handler.interceptor.DefaultTcpInterceptor;
import org.qiunet.flash.handler.interceptor.DefaultUdpInterceptor;
import org.qiunet.flash.handler.netty.client.tcp.NettyTcpClient;
import org.qiunet.flash.handler.netty.client.trigger.ILongConnResponseTrigger;
import org.qiunet.flash.handler.netty.client.udp.NettyUdpClient;
import org.qiunet.flash.handler.netty.server.BootstrapServer;
import org.qiunet.flash.handler.netty.server.hook.Hook;
import org.qiunet.flash.handler.netty.server.param.TcpBootstrapParams;
import org.qiunet.flash.handler.netty.server.param.UdpBootstrapParams;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.util.concurrent.locks.LockSupport;

/**
 * Created by qiunet.
 * 17/11/25
 */
public abstract class UdpBootStrap extends RequestHandlerScanner implements ILongConnResponseTrigger {
	protected static String host = "localhost";
	protected static int port = 8888;
	protected static Hook hook = new MyHook();
	protected NettyUdpClient udpClient;
	private static Thread currThread;
	@BeforeClass
	public static void init(){
		currThread = Thread.currentThread();
		Thread thread = new Thread(new Runnable() {
			@Override
			public void run() {
				UdpBootstrapParams udpParams = UdpBootstrapParams.custom()
						.setUdpInterceptor(new DefaultUdpInterceptor())
						.setErrorMessage(new DefaultErrorMessage())
						.setPort(port)
						.setCrc(true)
						.build();
				BootstrapServer server = BootstrapServer.createBootstrap(hook).udpListener(udpParams);
				LockSupport.unpark(currThread);
				server.await();
			}
		});
		thread.start();
		LockSupport.park();
	}
	@Before
	public void connect(){
		currThread = Thread.currentThread();
		try {
			udpClient = new NettyUdpClient(new InetSocketAddress(InetAddress.getByName(host), port), this,true);
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
	}
	@After
	public void closeConnect(){
		currThread = Thread.currentThread();
		LockSupport.park();
		udpClient.close();
	}

	@Override
	public void response(MessageContent data) {
		this.responseUdpMessage(data);
		LockSupport.unpark(currThread);
	}

	protected abstract void responseUdpMessage(MessageContent data);

	@AfterClass
	public static void shutdown() throws InterruptedException {
		BootstrapServer.sendHookMsg(hook.getHookPort(), hook.getShutdownMsg());
	}
}
