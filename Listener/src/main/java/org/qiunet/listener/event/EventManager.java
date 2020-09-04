package org.qiunet.listener.event;

public class EventManager {
	/***
	 * 触发监听处理
	 * @param data
	 */
	public static  <Data extends IEventData> void fireEventHandler(Data data) {
		EventManager0.getInstance().fireEventHandler(data);
	}
}
