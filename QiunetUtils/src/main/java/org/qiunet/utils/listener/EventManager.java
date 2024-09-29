package org.qiunet.utils.listener;

import org.qiunet.utils.exceptions.CustomException;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.function.BiConsumer;

public class EventManager {
	/***
	 * 触发监听处理
	 * 每个事件的异常只打印. 不往外抛出.
	 * @param event 事件数据
	 */
	public static  <Data extends IEventData> void post(Data event) {
		EventManager0.getInstance().post(event);
	}
	/***
	 * 触发监听处理
	 * 会往外抛出异常
	 * @param event 事件数据
	 */
	public static  <Data extends IEventData> void postAndThrowException(Data event) {
		EventManager0.getInstance().post(event, (m, e) -> {
			if (e instanceof IllegalAccessException) {
				throw new CustomException(e, "Fire Event Handler [{}.{}] Error!", m.getDeclaringClass().getName(), m.getName());
			}
			else if (e instanceof InvocationTargetException) {
				Throwable targetException = ((InvocationTargetException) e).getTargetException();
				if (! (targetException instanceof CustomException)) {
					throw new CustomException(targetException, "Fire Event Handler [{}.{}] Error!", m.getDeclaringClass().getName(), m.getName());
				}else {
					throw ((CustomException) targetException);
				}
			}else {
				throw new CustomException(e, "Fire Event Error!");
			}
		});
	}

	/**
	 * 触发事件 自己处理异常
	 * @param eventData 事件数据
	 * @param exConsume 异常消费
	 */
	public static <Data extends IEventData>  void post(Data eventData, BiConsumer<Method, Throwable> exConsume) {
		EventManager0.instance.post(eventData, exConsume);
	}
	/***
	 * 触发监听处理
	 * @param data
	 */
	public static <Data extends IEventData> void fireEventHandler(Data data) {
		EventManager0.getInstance().post(data);
	}
}
