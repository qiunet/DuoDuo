package org.qiunet.function.ai.node.root;

import org.qiunet.function.ai.enums.ActionStatus;
import org.qiunet.function.ai.node.IBehaviorExecutor;
import org.qiunet.function.ai.node.IBehaviorNode;
import org.qiunet.function.ai.node.base.BaseDecorator;
import org.qiunet.function.ai.node.executor.SelectorExecutor;
import org.qiunet.utils.exceptions.CustomException;

/***
 *  执行器ROOT
 *
 * qiunet
 * 2021/7/26 09:37
 **/
public final class BehaviorRootTree extends BaseDecorator {

	/**
	 * 默认使用 selector 节点作为root节点
	 */
	public BehaviorRootTree(){
		this(new SelectorExecutor(false));
	}
	/**
	 * 使用自定义的节点作为默认节点
	 * @param node
	 */
	public BehaviorRootTree(IBehaviorExecutor node) {
		super(node);
	}

	public void tick(){
		if (! node.isRunning()) {
			// 有的执行器需执行前清理状态. 等
			node.initialize();
		}
		node.run();
		// 不管SUCCESS 还是 FAILURE 都执行清理操作.
		if (! node.isRunning()) {
			node.release();
		}
	}


	@Override
	public IBehaviorExecutor addChild(IBehaviorNode... actions) {
		((IBehaviorExecutor) this.node).addChild(actions);
		return this;
	}

	@Override
	public ActionStatus run() {
		return node.run();
	}

	@Override
	protected ActionStatus execute() {
		throw new CustomException("Not a callable method");
	}
}
