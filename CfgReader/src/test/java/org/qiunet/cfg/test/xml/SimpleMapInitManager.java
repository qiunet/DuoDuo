package org.qiunet.cfg.test.xml;

import org.qiunet.cfg.manager.xml.SimpleMapXmlCfgManager;
import org.qiunet.cfg.test.InitCfg;

class SimpleMapInitManager extends SimpleMapXmlCfgManager<Integer, InitCfg> {
	private static SimpleMapInitManager instance = new SimpleMapInitManager();

	public static SimpleMapInitManager getInstance() {
		return instance;
	}

	private SimpleMapInitManager() {
		super("config/init/init_data.xml");
	}
}
