package org.qiunet.cfg.event;

import org.qiunet.cfg.manager.base.ICfgManager;
import org.qiunet.utils.listener.event.IListenerEvent;

/***
 * 添加ICfgManager 到 CfgManagers 事件
  *
 * @author qiunet
 * 2020-09-18 17:19
 */
public class CfgManagerAddEvent implements IListenerEvent {

	private ICfgManager cfgManager;

	public static void fireEvent(ICfgManager cfgManager) {
		CfgManagerAddEvent data = new CfgManagerAddEvent();
		data.cfgManager = cfgManager;
		data.fireEventHandler();
	}

	public ICfgManager getCfgManager() {
		return cfgManager;
	}
}
