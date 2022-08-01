package org.qiunet.game.test.bt;

import org.qiunet.flash.handler.common.MessageHandler;
import org.qiunet.function.ai.node.IBehaviorNode;

/***
 * 构造一个流程行为.
 * 比如登入 → (注册|注册) → 首页
 *
 * qiunet
 * 2021/7/21 09:59
 **/
public interface IBehaviorBuilder<Obj extends MessageHandler<Obj>> {
	/**
	 * 该行为的执行器
	 * @return
	 */
	IBehaviorNode<Obj> buildExecutor(Obj obj);
}
