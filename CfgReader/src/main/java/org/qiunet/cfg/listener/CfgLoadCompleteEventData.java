package org.qiunet.cfg.listener;


import org.qiunet.listener.event.IEventData;

/***
 * 配置加载完成事件
 *
 * @author qiunet
 * 2020-04-24 11:25
 ***/
public class CfgLoadCompleteEventData implements IEventData {
	public static void fireEvent(){
		new CfgLoadCompleteEventData().fireEventHandler();
	}
}
