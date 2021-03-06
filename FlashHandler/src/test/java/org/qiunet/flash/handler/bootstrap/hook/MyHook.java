package org.qiunet.flash.handler.bootstrap.hook;


import org.qiunet.flash.handler.netty.server.hook.Hook;
import org.qiunet.utils.logger.LoggerType;

/**
 * Created by qiunet.
 * 17/11/22
 */
public class MyHook implements Hook {

	@Override
	public String getReloadCfgMsg() {
		return "Reload";
	}

	@Override
	public void reloadCfg() {
	}

	@Override
	public int getHookPort() {
		return 1314;
	}

	@Override
	public String getShutdownMsg() {
		return "shutdown";
	}

	@Override
	public void shutdown() {
		LoggerType.DUODUO_FLASH_HANDLER.error("Called MyHook");
	}

	@Override
	public void custom(String msg) {

	}
}
