package org.qiunet.flash.handler.bootstrap.hook;

import org.qiunet.cfg.manager.CfgManagers;
import org.qiunet.flash.handler.netty.server.hook.BaseHook;

/**
 * Created by qiunet.
 * 17/11/22
 */
public class MyHook extends BaseHook {

	@Override
	public String getReloadCfgMsg() {
		return "Reload";
	}

	@Override
	public void reloadCfg() {
		try {
			CfgManagers.getInstance().reloadSetting();
		} catch (Exception e) {
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
		logger.error("Called MyHook");
	}

	@Override
	public Runnable returnCustomTask(String msg, String ip) {
		return () -> {
			switch (msg) {
				case "A":
					logger.error("A msg called");
					break;
				case "B":
					logger.error("B msg called");
					break;
			}
		};
	}
}
