package org.qiunet.flash.handler.bootstrap;

import org.junit.Test;
import org.qiunet.flash.handler.bootstrap.hook.MyHook;
import org.qiunet.flash.handler.netty.server.BootstrapServer;

/**
 * Created by qiunet.
 * 17/11/22
 */
public class TestBootstrapShutdown {

	public static void main(String[] args) {
		BootstrapServer.createBootstrap(new MyHook()).await();
	}


	@Test
	public void shutdown(){
		BootstrapServer.sendHookMsg(1314, "shutdown");
	}
}
