package org.qiunet.cfg.wrapper;

import org.qiunet.cfg.base.ISimpleMapCfg;
import org.qiunet.cfg.manager.base.ISimpleMapCfgManager;

import java.util.List;
import java.util.Map;

/***
 *
 *
 * @author qiunet
 * 2020-04-23 11:10
 ***/
 class SimpleMapCfgWrapper<ID, Cfg extends ISimpleMapCfg<ID>>
	extends BaseCfgWrapper<ID, Cfg> implements ISimpleMapCfgWrapper<ID, Cfg> {

	private final ISimpleMapCfgManager<ID, Cfg> cfgManager;

	 SimpleMapCfgWrapper(ISimpleMapCfgManager<ID, Cfg> cfgManager) {
		this.cfgManager = cfgManager;
	}

	@Override
	public Class<Cfg> getCfgClass() {
		return cfgManager.getCfgClass();
	}

	@Override
	public Map<ID, Cfg> allCfgs() {
		return cfgManager.allCfgs();
	}

	@Override
	public List<Cfg> list() {
		return this.cfgManager.list();
	}
}
