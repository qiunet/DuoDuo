package org.qiunet.function.ai.enums;

/***
 * 状态
 *
 * @author qiunet
 * 2021-07-05 14:24
 */
public enum ActionStatus {
	/**
	 * 成功
	 */
	SUCCESS,
	/**
	 * 失败
	 */
	FAILURE,
	/**
	 *  运行中.
	 *  运行中的状态 需要有结束机制.
	 *  比如行走中, 到达目标地点后. 需要调用
	 *
	 *  可能返回RUNNING的node .需要在run中判断Running状态. 并返回是否还是running . 或者 成功结束.
	 *  进行后续的操作.
	 */
	RUNNING,
}
