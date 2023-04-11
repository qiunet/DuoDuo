package org.qiunet.cfg.event;


import org.qiunet.cfg.manager.base.ICfgManager;
import org.qiunet.cfg.manager.base.ICfgWrapper;
import org.qiunet.utils.listener.event.IListenerEvent;

import java.util.List;

/***
 * 配置加载完成事件
 *
 * @author qiunet
 * 2020-04-24 11:25
 ***/
public class CfgLoadCompleteEvent implements IListenerEvent {
	private final List<ICfgWrapper> list;

	private CfgLoadCompleteEvent(List<ICfgWrapper> list) {
		this.list = list;
	}

	public static CfgLoadCompleteEvent valueOf(List<ICfgManager<?, ?>> list){
		List<ICfgWrapper> list1 = list.stream().map(m -> (ICfgWrapper) m).toList();
		return new CfgLoadCompleteEvent(list1);
	}

	public List<ICfgWrapper> getList() {
		return list;
	}
}
