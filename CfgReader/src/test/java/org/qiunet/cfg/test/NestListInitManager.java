package org.qiunet.cfg.test;

import org.qiunet.cfg.manager.xd.NestListXdCfgManager;

public class NestListInitManager extends NestListXdCfgManager<Integer, Init3Cfg> {
	private static NestListInitManager instance = new NestListInitManager();

	public static NestListInitManager getInstance() {
		return instance;
	}

	private NestListInitManager() {
		super("config/init/init_data.xd");
	}
}
