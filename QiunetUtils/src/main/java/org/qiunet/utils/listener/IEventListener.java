package org.qiunet.utils.listener;

/***
 * 有处理事件的类 实现该接口.
 * 实现该接口的类 应该有空构造函数.
 */
public interface IEventListener {
	/***
	 * 事件处理.
	 * @param eventData
	 */
	void eventHandler(IEventData eventData);
}
