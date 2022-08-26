package org.qiunet.test.handler.bootstrap.kcp;


import com.google.common.collect.Sets;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.qiunet.flash.handler.context.header.ProtocolHeaderType;
import org.qiunet.flash.handler.netty.server.BootstrapServer;
import org.qiunet.flash.handler.netty.server.hook.Hook;
import org.qiunet.flash.handler.netty.server.param.KcpBootstrapParams;
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
			KcpBootstrapParams params = KcpBootstrapParams.custom()
					.setProtocolHeaderType(ProtocolHeaderType.server)
					.setStartupContext(new StartupContext())
					.setPorts(Sets.newHashSet(port))
					.setServerName("游戏服")
					.setEncryption(true)
				.build();

			BootstrapServer server = BootstrapServer.createBootstrap(hook).kcpListener(params);
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
