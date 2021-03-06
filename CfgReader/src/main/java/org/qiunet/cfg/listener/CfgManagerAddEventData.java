package org.qiunet.cfg.listener;

import org.qiunet.cfg.manager.base.ICfgManager;
import org.qiunet.listener.event.IEventData;

/***
 * 添加ICfgManager 到 CfgManagers 事件
  *
 * @author qiunet
 * 2020-09-18 17:19
 */
public class CfgManagerAddEventData implements IEventData {

	private ICfgManager cfgManager;

	public static void fireEvent(ICfgManager cfgManager) {
		CfgManagerAddEventData data = new CfgManagerAddEventData();
		data.cfgManager = cfgManager;
		data.fireEventHandler();
	}

	public ICfgManager getCfgManager() {
		return cfgManager;
	}
}
