package org.qiunet.function.ai.builder;

import org.qiunet.function.ai.node.IBehaviorNode;

/***
 * 构造一个流程行为.
 * 比如登入 -> (注册|注册) -> 首页
 *
 * qiunet
 * 2021/7/21 09:59
 **/
public interface IBehaviorBuilder<Obj> {
	/**
	 * 该行为的执行器
	 * @return
	 */
	IBehaviorNode buildExecutor(Obj obj);
}
