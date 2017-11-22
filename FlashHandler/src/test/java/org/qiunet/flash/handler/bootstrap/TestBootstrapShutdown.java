package org.qiunet.flash.handler.bootstrap;

import org.junit.Test;
import org.qiunet.flash.handler.bootstrap.hook.MyShutdownHook;
import org.qiunet.flash.handler.netty.server.BootstrapServer;

/**
 * Created by qiunet.
 * 17/11/22
 */
public class TestBootstrapShutdown {

	public static void main(String[] args) {
		TestBootstrapShutdown testBootstrap = new TestBootstrapShutdown();
		testBootstrap.startShutdownListener();
	}

	public void startShutdownListener(){
		BootstrapServer.createBootstrap(1314, "shutdown", new MyShutdownHook()).await();
	}
	@Test
	public void shutdown(){
		BootstrapServer.sendShutdown(1314, "shutdown");
	}
}
