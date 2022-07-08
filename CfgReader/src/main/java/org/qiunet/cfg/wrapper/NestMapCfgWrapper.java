package org.qiunet.cfg.wrapper;


import org.qiunet.cfg.base.INestMapCfg;
import org.qiunet.cfg.manager.base.INestMapCfgManager;

import java.util.List;
import java.util.Map;

/***
 *
 *
 * @author qiunet
 * 2020-04-23 15:00
 ***/
 class NestMapCfgWrapper<ID, SubId, Cfg extends INestMapCfg<ID, SubId>>
	extends BaseCfgWrapper<ID, Cfg> implements INestMapCfgWrapper<ID, SubId, Cfg> {

	private final INestMapCfgManager<ID, SubId, Cfg> cfgManager;

	 NestMapCfgWrapper(INestMapCfgManager<ID, SubId, Cfg> cfgManager) {
		this.cfgManager = cfgManager;
	}
	@Override
	public Class<Cfg> getCfgClass() {
		return cfgManager.getCfgClass();
	}

	@Override
	public Map<ID, Map<SubId, Cfg>> allCfgs() {
		return cfgManager.allCfgs();
	}

	@Override
	public List<Cfg> list() {
		return cfgManager.list();
	}
}
