package org.qiunet.cfg.manager.base;

import org.qiunet.cfg.base.ISimpleMapCfg;
import org.qiunet.cfg.wrapper.ISimpleMapCfgWrapper;

/***
 *
 *
 * @author qiunet
 * 2020-04-23 11:54
 ***/
public interface ISimpleMapCfgManager<ID, Cfg extends ISimpleMapCfg<ID>>
	extends ICfgManager<Cfg>, ISimpleMapCfgWrapper<ID, Cfg> {

}
