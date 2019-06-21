package org.qiunet.utils.listener;

/***
 * 包装事件的类的接口
 *
 * 其它数据在自己的子类里面获取
 */
public interface IEventData {
	/***
	 * 触发时间处理
	 */
	default void fireEventHandler() {
		ListenerManager.fireEventHandler(this);
	}
}
