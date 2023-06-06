package org.qiunet.utils.listener.event;

import java.lang.reflect.Method;
import java.util.function.BiConsumer;

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
	public static  <Data extends IListenerEvent> void post(Data data, BiConsumer<Method, Throwable> exConsume) {
		EventManager0.getInstance().post(data, exConsume);
	}
	/***
	 * 触发监听处理
	 * @param data
	 */
	public static  <Data extends IListenerEvent> void fireEventHandler(Data data) {
		EventManager0.getInstance().post(data);
	}
}
