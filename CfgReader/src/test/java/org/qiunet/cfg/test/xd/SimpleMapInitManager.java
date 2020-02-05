package org.qiunet.cfg.test.xd;

import org.qiunet.cfg.manager.xd.SimpleMapXdCfgManager;
import org.qiunet.cfg.test.InitCfg;

class SimpleMapInitManager extends SimpleMapXdCfgManager<Integer, InitCfg> {
	private static SimpleMapInitManager instance = new SimpleMapInitManager();

	public static SimpleMapInitManager getInstance() {
		return instance;
	}

	private SimpleMapInitManager() {
		super("config/init/init_data.xd");
	}
}
