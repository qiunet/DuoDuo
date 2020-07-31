package org.qiunet.cfg.wrapper;

import org.qiunet.cfg.base.INestListCfg;
import org.qiunet.cfg.manager.base.INestListCfgManager;

import java.util.List;
import java.util.Map;

/***
 *
 *
 * @author qiunet
 * 2020-04-23 16:28
 ***/
 class NestListCfgWrapper<ID, Cfg extends INestListCfg<ID>>
	extends BaseCfgWrapper<Cfg> implements INestListCfgWrapper<ID, Cfg> {
	private INestListCfgManager<ID, Cfg> cfgManager;

	 NestListCfgWrapper(INestListCfgManager<ID, Cfg> cfgManager) {
		this.cfgManager = cfgManager;
	}

	@Override
	protected Class<Cfg> getCfgClass() {
		return cfgManager.getCfgClass();
	}

	@Override
	public Map<ID, List<Cfg>> allCfgs() {
		return cfgManager.allCfgs();
	}
}
