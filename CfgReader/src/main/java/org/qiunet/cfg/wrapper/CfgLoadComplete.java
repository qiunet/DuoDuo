package org.qiunet.cfg.wrapper;

import org.qiunet.cfg.base.IAfterLoad;
import org.qiunet.cfg.base.ICfgCheck;
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
		// 先load 完毕. 再check .避免check调用其它cfg内容. 但是load里面没有赋值.
		CfgType.cfgWrappers.values().forEach(w -> ((IAfterLoad) w).afterLoad());
		CfgType.cfgWrappers.values().forEach(w -> ((ICfgCheck) w).check());
	}
}
