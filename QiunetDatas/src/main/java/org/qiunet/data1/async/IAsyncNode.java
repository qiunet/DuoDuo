package org.qiunet.data1.async;

/**
 * 异步更新节点
 * @author qiunet
 *         Created on 17/2/10 17:55.
 */
public interface IAsyncNode {
	/***
	 * 更新到db
	 */
	void syncToDatabase();
}
