package org.qiunet.cfg.wrapper;

import org.qiunet.cfg.base.IAfterLoad;
import org.qiunet.cfg.base.ICfg;
import org.qiunet.cfg.base.ICfgCheck;

/***
 *
 *
 * @author qiunet
 * 2020-04-24 11:19
 ***/
public abstract class BaseCfgWrapper<ID, Cfg extends ICfg<ID>> implements ICfgWrapper<ID, Cfg> , IAfterLoad , ICfgCheck {

	@Override
	public void check() {
		if (ICfgCheck.class.isAssignableFrom(getCfgClass())) {
			list().forEach(ele -> ((ICfgCheck) ele).check());
		}
	}

	@Override
	public void afterLoad() {
		if (IAfterLoad.class.isAssignableFrom(getCfgClass())) {
			list().forEach(ele -> ((IAfterLoad) ele).afterLoad());
		}
	}
}
