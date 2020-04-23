package org.qiunet.cfg.wrapper;

import org.qiunet.cfg.base.ISimpleMapCfg;
import org.qiunet.cfg.manager.base.ISimpleMapCfgManager;

import java.util.Map;

/***
 *
 *
 * @author qiunet
 * 2020-04-23 11:10
 ***/
public class SimpleMapCfgWrapper<ID, Cfg extends ISimpleMapCfg<ID>>
	implements ISimpleMapCfgWrapper<ID, Cfg> {

	private ISimpleMapCfgManager<ID, Cfg> cfgManager;

	public SimpleMapCfgWrapper(ISimpleMapCfgManager<ID, Cfg> cfgManager) {
		this.cfgManager = cfgManager;
	}

	@Override
	public Map<ID, Cfg> allCfgs() {
		return cfgManager.allCfgs();
	}
}
