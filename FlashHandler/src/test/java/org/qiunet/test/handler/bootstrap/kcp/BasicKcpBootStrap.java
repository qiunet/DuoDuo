package org.qiunet.test.handler.bootstrap.kcp;


import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.qiunet.flash.handler.netty.server.BootstrapServer;
import org.qiunet.flash.handler.netty.server.config.ServerBootStrapConfig;
import org.qiunet.flash.handler.netty.server.hook.Hook;
import org.qiunet.test.handler.bootstrap.hook.MyHook;
import org.qiunet.test.handler.startup.context.StartupContext;
import org.qiunet.utils.scanner.ClassScanner;
import org.qiunet.utils.scanner.ScannerType;

import java.util.concurrent.locks.LockSupport;

/**
 * Created by qiunet.
 * 17/11/25
 */
public abstract class BasicKcpBootStrap {
	protected static final String host = "localhost";
	protected static final int port = 8888;
	protected static final Hook hook = new MyHook();
	protected static Thread currThread;

	@BeforeAll
	public static void init() throws Exception {
		ClassScanner.getInstance(ScannerType.SERVER).scanner();

		currThread = Thread.currentThread();
		Thread thread = new Thread(() -> {
			ServerBootStrapConfig config = ServerBootStrapConfig.newBuild("游戏服", port)
					.setKcpBootStrapConfig(ServerBootStrapConfig.KcpBootstrapConfig.newBuild().setPortCount(0, 1).build())
					.setStartupContext(new StartupContext())
					.encryption()
				.build();

			BootstrapServer server = BootstrapServer.createBootstrap(hook).listener(config);
			server.await(() -> LockSupport.unpark(currThread));
		});
		thread.start();
		LockSupport.park();
	}

	@AfterAll
	public static void shutdown() {
		System.gc();
		BootstrapServer.sendHookMsg(hook.getHookPort(), hook.getShutdownMsg());
	}
}
