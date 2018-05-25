package org.qiunet.flash.handler.bootstrap.hook;

import org.qiunet.flash.handler.gamecfg.GameCfgManagers;
import org.qiunet.flash.handler.netty.server.hook.Hook;
import org.qiunet.utils.logger.LoggerType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by qiunet.
 * 17/11/22
 */
public class MyHook implements Hook {
	Logger qLogger = LoggerFactory.getLogger(LoggerType.DUODUO);

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
		qLogger.error("Called MyHook");
	}

	@Override
	public void custom(String msg, String ip) {
		switch (msg) {
			case "A":
				qLogger.error("A msg called");
				break;
			case "B":
				qLogger.error("B msg called");
				break;
		}
	}
}
