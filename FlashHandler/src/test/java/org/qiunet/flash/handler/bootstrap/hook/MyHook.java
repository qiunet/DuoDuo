package org.qiunet.flash.handler.bootstrap.hook;

import org.qiunet.flash.handler.gamecfg.GameCfgManagers;
import org.qiunet.flash.handler.netty.server.hook.BaseHook;
import org.qiunet.flash.handler.netty.server.hook.Hook;
import org.qiunet.utils.logger.LoggerType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
			GameCfgManagers.getInstance().reloadSetting();
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
