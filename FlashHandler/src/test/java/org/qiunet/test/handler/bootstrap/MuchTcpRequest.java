package org.qiunet.test.handler.bootstrap;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.qiunet.flash.handler.context.header.ProtocolHeaderType;
import org.qiunet.flash.handler.netty.server.BootstrapServer;
import org.qiunet.flash.handler.netty.server.hook.Hook;
import org.qiunet.flash.handler.netty.server.param.TcpBootstrapParams;
import org.qiunet.test.handler.bootstrap.hook.MyHook;
import org.qiunet.test.handler.startup.context.StartupContext;
import org.qiunet.utils.scanner.ClassScanner;

import java.util.concurrent.locks.LockSupport;

/**
 * Created by qiunet.
 * 17/11/27
 */
public abstract class MuchTcpRequest {
	protected static String host = "localhost";
	protected static int port = 8889;
	protected static Hook hook = new MyHook();
	private static Thread currThread;
	@BeforeClass
	public static void init() throws Exception {
		ClassScanner.getInstance().scanner();

		currThread = Thread.currentThread();
		Thread thread = new Thread(() -> {
			TcpBootstrapParams tcpParams = TcpBootstrapParams.custom()
				.setProtocolHeaderType(ProtocolHeaderType.server)
				.setStartupContext(new StartupContext())
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
		});
		thread.start();
		LockSupport.park();
	}

	@AfterClass
	public static void shutdown() {
		BootstrapServer.sendHookMsg(hook.getHookPort(), hook.getShutdownMsg());
	}
}


