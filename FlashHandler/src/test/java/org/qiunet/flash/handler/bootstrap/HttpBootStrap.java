package org.qiunet.flash.handler.bootstrap;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.qiunet.flash.handler.bootstrap.error.DefaultErrorMessage;
import org.qiunet.flash.handler.bootstrap.hook.MyHook;
import org.qiunet.flash.handler.handler.mapping.RequestHandlerScanner;
import org.qiunet.flash.handler.interceptor.DefaultHttpInterceptor;
import org.qiunet.flash.handler.interceptor.DefaultWebSocketInterceptor;
import org.qiunet.flash.handler.netty.server.BootstrapServer;
import org.qiunet.flash.handler.netty.server.hook.Hook;
import org.qiunet.flash.handler.netty.server.param.HttpBootstrapParams;

import java.util.concurrent.locks.LockSupport;

/**
 * Created by qiunet.
 * 17/11/25
 */
public class HttpBootStrap extends RequestHandlerScanner {
	private static Hook hook = new MyHook();
	private static Thread currThread;
	@BeforeClass
	public static void init() {
		currThread = Thread.currentThread();
		Thread thread = new Thread(new Runnable() {
			@Override
			public void run() {
				HttpBootstrapParams httpParams = HttpBootstrapParams.custom()
						.setWebSocketInterceptor(new DefaultWebSocketInterceptor())
						.setHttpInterceptor(new DefaultHttpInterceptor())
						.setErrorMessage(new DefaultErrorMessage())
						.setWebsocketPath("/ws")
						.setPort(8080)
						.build();
				BootstrapServer server = BootstrapServer.createBootstrap(hook).httpListener(httpParams);
				LockSupport.unpark(currThread);
				server.await();
			}
		});
		thread.start();
		LockSupport.park();
	}

	@AfterClass
	public static void shutdown(){
		BootstrapServer.sendHookMsg(hook.getHookPort(), hook.getShutdownMsg());
	}
}
