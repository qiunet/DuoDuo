package org.qiunet.utils.listener.event;

public class EventManager {
	/***
	 * 触发监听处理
	 * @param data
	 */
	public static  <Data extends IEventData> void post(Data data) {
		EventManager0.getInstance().post(data);
	}
	/***
	 * 触发监听处理
	 * @param data
	 */
	public static  <Data extends IEventData> void fireEventHandler(Data data) {
		EventManager0.getInstance().post(data);
	}
}
