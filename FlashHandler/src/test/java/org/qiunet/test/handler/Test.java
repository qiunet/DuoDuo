package org.qiunet.test.handler;

import org.qiunet.flash.handler.netty.server.BootstrapServer;
import org.qiunet.flash.handler.netty.server.hook.Hook;
import org.qiunet.flash.handler.netty.server.param.ServerBootStrapParam;
import org.qiunet.test.handler.bootstrap.hook.MyHook;
import org.qiunet.test.handler.startup.context.StartupContext;
import org.qiunet.utils.scanner.ClassScanner;
import org.qiunet.utils.scanner.ScannerType;

/**
 * 用来debug 跟踪内部测试用
 * Created by qiunet.
 * 17/12/15
 */
public class Test {
	private static final Hook hook = new MyHook();
	public static void main(String[] args) throws Exception {
		ClassScanner.getInstance(ScannerType.SERVER).scanner();

		BootstrapServer.createBootstrap(hook).listener(
				ServerBootStrapParam.newBuild("测试", 8888)
					.setStartupContext(new StartupContext())
					.setEncryption(true)
					.build()
		).await();
	}
}
