package org.qiunet.flash.handler.bootstrap;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.qiunet.flash.handler.bootstrap.hook.MyHook;
import org.qiunet.flash.handler.handler.mapping.RequestHandlerScanner;
import org.qiunet.flash.handler.interceptor.DefaultHttpInterceptor;
import org.qiunet.flash.handler.netty.server.BootstrapServer;
import org.qiunet.flash.handler.netty.server.hook.Hook;
import org.qiunet.flash.handler.netty.server.param.HttpBootstrapParams;

/**
 * Created by qiunet.
 * 17/11/25
 */
public class HttpBootStrap extends RequestHandlerScanner {
	private static Hook hook = new MyHook();
	@BeforeClass
	public static void init() {
		Thread thread = new Thread(new Runnable() {
			@Override
			public void run() {
				HttpBootstrapParams httpParams = HttpBootstrapParams.custom()
						.setInterceptor(new DefaultHttpInterceptor())
						.setPort(8080)
						.build();
				BootstrapServer.createBootstrap(hook).httpListener(httpParams).await();
			}
		});
		thread.start();
	}

	@AfterClass
	public static void shutdown(){
		BootstrapServer.sendHookMsg(hook.getHookPort(), hook.getShutdownMsg());
	}
}
