package org.qiunet.test.cross.server;


import org.qiunet.flash.handler.netty.server.hook.Hook;

/**
 * Created by qiunet.
 * 17/11/22
 */
public class MyHook implements Hook {
	private int port;

	public MyHook(int port) {
		this.port = port;
	}

	@Override
	public String getReloadCfgMsg() {
		return "Reload";
	}

	@Override
	public void reloadCfg() {
	}

	@Override
	public int getHookPort() {
		return port;
	}

	@Override
	public String getShutdownMsg() {
		return "shutdown";
	}

	@Override
	public void shutdown() {
	}

	@Override
	public void custom(String msg) {

	}
}
