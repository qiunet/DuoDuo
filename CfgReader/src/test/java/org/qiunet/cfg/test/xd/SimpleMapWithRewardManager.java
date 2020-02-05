package org.qiunet.cfg.test.xd;

import org.qiunet.cfg.manager.xd.SimpleMapXdCfgManager;
import org.qiunet.cfg.test.InitWithRewardCfg;

class SimpleMapWithRewardManager extends SimpleMapXdCfgManager<Integer, InitWithRewardCfg> {
	private static SimpleMapWithRewardManager instance = new SimpleMapWithRewardManager();

	public static SimpleMapWithRewardManager getInstance() {
		return instance;
	}

	private SimpleMapWithRewardManager() {
		super("config/init/init_data.xd");
	}
}
