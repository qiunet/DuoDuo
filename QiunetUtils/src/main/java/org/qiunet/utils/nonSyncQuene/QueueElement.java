package org.qiunet.utils.nonSyncQuene;
/**
 * 处理的对象
 * @author qiunet
 *
 */
public interface QueueElement {
	/**
	 * 处理逻辑
	 * @return
	 */
	public boolean handler();
	/**
	 * boolean 返回false 时候. 我会打印
	 * @return
	 */
	public String toStr();
}
