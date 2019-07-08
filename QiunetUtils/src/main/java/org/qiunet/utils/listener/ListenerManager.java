package org.qiunet.utils.listener;

public class ListenerManager {
	/***
	 * 触发监听处理
	 * @param data
	 */
	public static  <Data extends IEventData> void fireEventHandler(Data data) {
		ListenerManager0.getInstance().fireEventHandler(data);
	}
}
