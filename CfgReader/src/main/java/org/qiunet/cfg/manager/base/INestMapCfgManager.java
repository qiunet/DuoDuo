package org.qiunet.cfg.manager.base;

import org.qiunet.cfg.base.INestMapCfg;
import org.qiunet.cfg.wrapper.INestMapCfgWrapper;

/***
 *
 *
 * @author qiunet
 * 2020-04-23 11:54
 ***/
public interface INestMapCfgManager<ID, SubID, Cfg extends INestMapCfg<ID, SubID>>
	extends ICfgManager<ID, Cfg>, INestMapCfgWrapper<ID, SubID, Cfg> {

}
