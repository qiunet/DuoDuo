package org.qiunet.data.async;

/**
 * 异步更新节点
 * @author qiunet
 *         Created on 17/2/10 17:55.
 */
public interface AsyncNode {
	/***
	 * 更新到db
	 */
	public void updateRedisDataToDatabase ();
}
