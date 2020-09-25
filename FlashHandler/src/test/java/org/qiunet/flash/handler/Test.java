package org.qiunet.flash.handler;

import org.qiunet.flash.handler.bootstrap.hook.MyHook;
import org.qiunet.flash.handler.netty.server.BootstrapServer;
import org.qiunet.flash.handler.netty.server.hook.Hook;
import org.qiunet.flash.handler.netty.server.param.TcpBootstrapParams;
import org.qiunet.flash.handler.startup.context.StartupContext;
import org.qiunet.utils.scanner.ClassScanner;

/**
 * 用来debug 跟踪内部测试用
 * Created by qiunet.
 * 17/12/15
 */
public class Test {
	private static Hook hook = new MyHook();
	public static void main(String[] args) throws Exception {
		ClassScanner.getInstance().scanner();

		BootstrapServer.createBootstrap(hook).tcpListener(
				TcpBootstrapParams.custom()
						.setStartupContext(new StartupContext())
						.setPort(8888)
						.setEncryption(true)
					.build()
		).await();
	}
}
