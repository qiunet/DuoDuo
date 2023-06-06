package org.qiunet.utils.listener.event;

import java.lang.reflect.Method;
import java.util.function.BiConsumer;

/***
 * 包装事件的类的接口
 *
 * 其它数据在自己的子类里面获取
 * @author qiunet
 */
public interface IListenerEvent {
	/***
	 * 触发事件处理
	 */
	default void fireEventHandler() {
		EventManager.post(this);
	}
	/***
	 * 触发事件处理
	 */
	default void fireEventHandler(BiConsumer<Method, Throwable> exConsume) {
		EventManager.post(this, exConsume);
	}
}
