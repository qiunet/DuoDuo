package org.qiunet.cfg.test;

import org.qiunet.cfg.manager.xd.NestMapXdCfgManager;

public class NestMapInitManager extends NestMapXdCfgManager<Integer, String, Init2Cfg> {
	private static NestMapInitManager instance = new NestMapInitManager();

	public static NestMapInitManager getInstance() {
		return instance;
	}

	private NestMapInitManager() {
		super("config/init/init_data.xd");
	}
}
