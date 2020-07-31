package org.qiunet.cfg.wrapper;

import org.qiunet.cfg.base.IAfterLoad;
import org.qiunet.cfg.listener.CfgLoadCompleteEventData;
import org.qiunet.utils.classScanner.Singleton;

/***
 *
 *
 * @author qiunet
 * 2020-04-24 14:25
 ***/
@Singleton
class CfgLoadComplete implements CfgLoadCompleteEventData.CfgLoadCompleteListener {
	@Override
	public void loadComplete(CfgLoadCompleteEventData data) {
		CfgType.cfgWrappers.values().forEach(w -> ((IAfterLoad) w).afterLoad());
	}
}
