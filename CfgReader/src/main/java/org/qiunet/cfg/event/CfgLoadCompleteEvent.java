package org.qiunet.cfg.event;


import org.qiunet.cfg.manager.base.ICfgManager;
import org.qiunet.utils.listener.event.IEventData;

import java.util.List;

/***
 * 配置加载完成事件
 *
 * @author qiunet
 * 2020-04-24 11:25
 ***/
public class CfgLoadCompleteEvent implements IEventData {
	private final List<ICfgManager<?, ?>> list;

	private CfgLoadCompleteEvent(List<ICfgManager<?, ?>> list) {
		this.list = list;
	}

	public static CfgLoadCompleteEvent valueOf(List<ICfgManager<?, ?>> list){
		return new CfgLoadCompleteEvent(list);
	}

	public List<ICfgManager<?, ?>> getList() {
		return list;
	}
}
