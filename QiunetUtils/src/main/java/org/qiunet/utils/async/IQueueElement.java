package org.qiunet.utils.async;
/**
 * 处理的对象
 * @author qiunet
 *
 */
public interface IQueueElement {
	/**
	 * 处理逻辑
	 * @return
	 */
	 boolean handler();
	/**
	 * boolean 返回false 时候. 我会打印
	 * @return
	 */
	 default String toStr() {
	 	return "";
	 }
}
