package org.qiunet.game.test.behavior.action;

/***
 * 测试节点.
 *
 * @author qiunet
 * 2021-07-05 09:57
 */
public interface IBehaviorNode {
	/**
	 * 运行节点.
	 * @return 成功 或者 失败
	 */
	void run();

	/**
	 * 状态
	 * @return
	 */
	ActionStatus getStatus();
	/**
	 * 父节点
	 * @return
	 */
	IBehaviorNode parent();
}
