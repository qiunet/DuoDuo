package org.qiunet.cfg.wrapper;

import org.qiunet.cfg.base.IAfterLoad;
import org.qiunet.cfg.listener.CfgLoadCompleteEventData;
import org.qiunet.listener.event.EventListener;

/***
 *
 *
 * @author qiunet
 * 2020-04-24 14:25
 ***/
class CfgLoadComplete{
	@EventListener
	public void loadComplete(CfgLoadCompleteEventData data) {
		CfgType.cfgWrappers.values().forEach(w -> ((IAfterLoad) w).afterLoad());
	}
}
