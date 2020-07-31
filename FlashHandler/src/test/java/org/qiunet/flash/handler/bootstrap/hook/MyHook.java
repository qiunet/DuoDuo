package org.qiunet.flash.handler.bootstrap.hook;


import org.qiunet.cfg.manager.CfgManagers;
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
		try {
			CfgManagers.getInstance().reloadSetting();
		} catch (Throwable e) {
			e.printStackTrace();
		}
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
		LoggerType.DUODUO.error("Called MyHook");
	}

	@Override
	public void custom(String msg) {

	}
}
