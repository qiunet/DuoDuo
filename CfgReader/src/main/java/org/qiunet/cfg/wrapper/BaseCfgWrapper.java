package org.qiunet.cfg.wrapper;

import org.qiunet.cfg.base.IAfterLoad;
import org.qiunet.cfg.base.ICfg;

/***
 *
 *
 * @author qiunet
 * 2020-04-24 11:19
 ***/
public abstract class BaseCfgWrapper<T extends ICfg> implements ICfgWrapper<T> , IAfterLoad {
	protected abstract Class<T> getCfgClass();

	@Override
	public void afterLoad() {
		if (IAfterLoad.class.isAssignableFrom(getCfgClass())) {
			list().forEach(ele -> ((IAfterLoad) ele).afterLoad());
		}
	}
}
