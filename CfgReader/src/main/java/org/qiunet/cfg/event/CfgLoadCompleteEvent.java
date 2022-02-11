package org.qiunet.cfg.event;


import org.qiunet.utils.listener.event.IEventData;

/***
 * 配置加载完成事件
 *
 * @author qiunet
 * 2020-04-24 11:25
 ***/
public class CfgLoadCompleteEvent implements IEventData {
	public static void fireEvent(){
		new CfgLoadCompleteEvent().fireEventHandler();
	}
}
