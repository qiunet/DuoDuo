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
public class NestListCfgWrapper<ID, Cfg extends INestListCfg<ID>> implements INestListCfgWrapper<ID, Cfg> {
	private INestListCfgManager<ID, Cfg> cfgManager;

	public NestListCfgWrapper(INestListCfgManager<ID, Cfg> cfgManager) {
		this.cfgManager = cfgManager;
	}

	@Override
	public Map<ID, List<Cfg>> allCfgs() {
		return cfgManager.allCfgs();
	}
}
