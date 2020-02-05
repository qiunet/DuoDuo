package org.qiunet.cfg.test.xml;


import org.qiunet.cfg.manager.xml.SimpleMapXmlCfgManager;
import org.qiunet.cfg.test.InitWithRewardCfg;

class SimpleMapWithRewardManager extends SimpleMapXmlCfgManager<Integer, InitWithRewardCfg> {
	private static SimpleMapWithRewardManager instance = new SimpleMapWithRewardManager();

	public static SimpleMapWithRewardManager getInstance() {
		return instance;
	}

	private SimpleMapWithRewardManager() {
		super("config/init/init_data.xml");
	}
}
