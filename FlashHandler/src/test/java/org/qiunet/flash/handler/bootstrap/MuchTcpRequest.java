package org.qiunet.flash.handler.bootstrap;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.qiunet.flash.handler.bootstrap.error.DefaultErrorMessage;
import org.qiunet.flash.handler.bootstrap.hook.MyHook;
import org.qiunet.flash.handler.handler.mapping.RequestHandlerScanner;
import org.qiunet.flash.handler.interceptor.DefaultTcpInterceptor;
import org.qiunet.flash.handler.netty.server.BootstrapServer;
import org.qiunet.flash.handler.netty.server.hook.Hook;
import org.qiunet.flash.handler.netty.server.param.TcpBootstrapParams;

import java.util.concurrent.locks.LockSupport;

/**
 * Created by qiunet.
 * 17/11/27
 */
public abstract class MuchTcpRequest extends RequestHandlerScanner {
	protected static String host = "localhost";
	protected static int port = 8889;
	protected static Hook hook = new MyHook();
	private static Thread currThread;
	@BeforeClass
	public static void init(){
		currThread = Thread.currentThread();
		Thread thread = new Thread(new Runnable() {
			@Override
			public void run() {
				TcpBootstrapParams tcpParams = TcpBootstrapParams.custom()
						.setTcpInterceptor(new DefaultTcpInterceptor())
						.setErrorMessage(new DefaultErrorMessage())
						.setPort(port)
						.build();
				BootstrapServer server = BootstrapServer.createBootstrap(hook).tcpListener(tcpParams);
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				LockSupport.unpark(currThread);
				server.await();
			}
		});
		thread.start();
		LockSupport.park();
	}

	@AfterClass
	public static void shutdown() throws InterruptedException {
		BootstrapServer.sendHookMsg(hook.getHookPort(), hook.getShutdownMsg());
	}
}


