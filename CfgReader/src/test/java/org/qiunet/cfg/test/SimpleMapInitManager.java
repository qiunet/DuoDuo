package org.qiunet.cfg.test;

import org.qiunet.cfg.manager.xd.SimpleMapXdCfgManager;

public class SimpleMapInitManager extends SimpleMapXdCfgManager<Integer, InitCfg> {
	private static SimpleMapInitManager instance = new SimpleMapInitManager();

	public static SimpleMapInitManager getInstance() {
		return instance;
	}

	private SimpleMapInitManager() {
		super("config/init/init_data.xd");
	}
}
