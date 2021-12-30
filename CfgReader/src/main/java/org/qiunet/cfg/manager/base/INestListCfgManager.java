package org.qiunet.cfg.manager.base;

import org.qiunet.cfg.base.INestListCfg;
import org.qiunet.cfg.wrapper.INestListCfgWrapper;

/***
 *
 *
 * @author qiunet
 * 2020-04-23 11:54
 ***/
public interface INestListCfgManager<ID, Cfg extends INestListCfg<ID>>
	extends ICfgManager<ID, Cfg>, INestListCfgWrapper<ID, Cfg> {

}
