package org.qiunet.flash.handler.bootstrap;


import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.qiunet.flash.handler.bootstrap.hook.MyHook;
import org.qiunet.flash.handler.handler.mapping.RequestHandlerScanner;
import org.qiunet.flash.handler.interceptor.DefaultTcpInterceptor;
import org.qiunet.flash.handler.netty.server.BootstrapServer;
import org.qiunet.flash.handler.netty.server.hook.Hook;
import org.qiunet.flash.handler.netty.server.param.TcpBootstrapParams;

/**
 * Created by qiunet.
 * 17/11/25
 */
public class TcpBootStrap extends RequestHandlerScanner {
	protected static Hook hook = new MyHook();
	protected static int port = 8888;
	@BeforeClass
	public static void init(){
		Thread thread = new Thread(new Runnable() {
			@Override
			public void run() {
				TcpBootstrapParams tcpParams = TcpBootstrapParams.custom()
						.setInterceptor(new DefaultTcpInterceptor())
						.setPort(port)
						.setCrc(true)
						.build();
				BootstrapServer.createBootstrap(hook).tcpListener(tcpParams).await();
			}
		});
		thread.start();
		try {
			Thread.sleep(500);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	@AfterClass
	public static void shutdown(){
		BootstrapServer.sendHookMsg(hook.getHookPort(), hook.getShutdownMsg());
	}
}
