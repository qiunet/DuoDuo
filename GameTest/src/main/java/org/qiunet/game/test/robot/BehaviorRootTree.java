package org.qiunet.game.test.robot;

import org.qiunet.function.condition.ConditionManager;
import org.qiunet.function.condition.IConditions;
import org.qiunet.game.test.behavior.node.executor.Selector;

/***
 *  执行器ROOT
 *
 * qiunet
 * 2021/7/26 09:37
 **/
final class BehaviorRootTree extends Selector {

	public BehaviorRootTree() {
		super((IConditions<Robot>) ConditionManager.EMPTY_CONDITION, "ROOT");
	}

	public void tick(){
		if (! isRunning()) {
			// 有的执行器需执行前清理状态. 等
			this.initialize();
		}
		super.run();
		// 不管SUCCESS 还是 FAILURE 都执行清理操作.
		if (! this.isRunning()) {
			this.release();
		}
	}
}
