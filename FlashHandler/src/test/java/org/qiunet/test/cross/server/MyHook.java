package org.qiunet.test.cross.server;


import org.qiunet.flash.handler.netty.server.hook.DefaultHook;

/**
 * Created by qiunet.
 * 17/11/22
 */

public class MyHook extends DefaultHook {

	private final int port;

	public MyHook(int port) {
		this.port = port;
	}

	@Override
	public int getHookPort() {
		return port;
	}

}
