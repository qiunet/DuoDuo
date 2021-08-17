package org.qiunet.function.ai.node.root;

import org.qiunet.function.ai.node.executor.Selector;

/***
 *  执行器ROOT
 *
 * qiunet
 * 2021/7/26 09:37
 **/
public final class BehaviorRootTree extends Selector {

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
