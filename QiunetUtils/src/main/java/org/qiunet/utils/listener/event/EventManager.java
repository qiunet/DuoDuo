package org.qiunet.utils.listener.event;

public class EventManager {
	/***
	 * 触发监听处理
	 * @param data
	 */
	public static  <Data extends IListenerEvent> void post(Data data) {
		EventManager0.getInstance().post(data);
	}
	/***
	 * 触发监听处理
	 * @param data
	 */
	public static  <Data extends IListenerEvent> void fireEventHandler(Data data) {
		EventManager0.getInstance().post(data);
	}
}
