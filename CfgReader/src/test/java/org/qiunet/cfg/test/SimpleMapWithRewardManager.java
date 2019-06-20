package org.qiunet.cfg.test;

import org.qiunet.cfg.manager.xd.SimpleMapXdCfgManager;

public class SimpleMapWithRewardManager extends SimpleMapXdCfgManager<Integer, InitWithRewardCfg> {
	private static SimpleMapWithRewardManager instance = new SimpleMapWithRewardManager();

	public static SimpleMapWithRewardManager getInstance() {
		return instance;
	}

	private SimpleMapWithRewardManager() {
		super("config/init/init_data.xd");
	}
}
