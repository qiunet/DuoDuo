package org.qiunet.flash.handler.bootstrap;

import org.qiunet.flash.handler.bootstrap.hook.MyShutdownHook;
import org.qiunet.flash.handler.netty.server.BootstrapServer;

/**
 * Created by qiunet.
 * 17/11/22
 */
public class TestBootstrap {

	public static void main(String[] args) {
		TestBootstrap testBootstrap = new TestBootstrap();
		testBootstrap.startShutdownListener();
	}

	public void startShutdownListener(){
		BootstrapServer.createBootstrap(1314, "shutdown", new MyShutdownHook()).await();
	}
}
