package org.qiunet.flash.handler.bootstrap;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.qiunet.flash.handler.bootstrap.hook.MyHook;
import org.qiunet.flash.handler.context.header.DefaultProtocolHeaderAdapter;
import org.qiunet.flash.handler.netty.server.BootstrapServer;
import org.qiunet.flash.handler.netty.server.hook.Hook;
import org.qiunet.flash.handler.netty.server.param.HttpBootstrapParams;
import org.qiunet.flash.handler.netty.server.param.adapter.IProtocolHeaderAdapter;
import org.qiunet.flash.handler.startup.context.StartupContext;
import org.qiunet.utils.scanner.ClassScanner;

import java.util.concurrent.locks.LockSupport;

/**
 * Created by qiunet.
 * 17/11/25
 */
public class HttpBootStrap {
	protected static final IProtocolHeaderAdapter ADAPTER = new DefaultProtocolHeaderAdapter();
	protected static final int port = 8080;
	private static Hook hook = new MyHook();
	private static Thread currThread;
	@BeforeClass
	public static void init() throws Exception {
		ClassScanner.getInstance().scanner();

		currThread = Thread.currentThread();
		Thread thread = new Thread(() -> {
			HttpBootstrapParams httpParams = HttpBootstrapParams.custom()
					.setStartupContext(new StartupContext())
					.setWebsocketPath("/ws")
					.setPort(port)
					.build();
			BootstrapServer server = BootstrapServer.createBootstrap(hook).httpListener(httpParams);
			LockSupport.unpark(currThread);
			server.await();
		});
		thread.start();
		LockSupport.park();
	}

	@AfterClass
	public static void shutdown(){
		BootstrapServer.sendHookMsg(hook.getHookPort(), hook.getShutdownMsg());
	}
}
