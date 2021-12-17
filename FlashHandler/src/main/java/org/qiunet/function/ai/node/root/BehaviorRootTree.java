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
public final class BehaviorRootTree<Owner> extends BaseDecorator<Owner> {
	private final Owner owner;
	/**
	 * 默认使用 selector 节点作为root节点
	 */
	public BehaviorRootTree(Owner owner){
		super(new SelectorExecutor<>(null, "Root"));
		this.owner = owner;
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
	public IBehaviorExecutor<Owner> addChild(IBehaviorNode<Owner>... actions) {
		((IBehaviorExecutor<Owner>) this.node).addChild(actions);
		return this;
	}

	@Override
	public Owner getOwner() {
		return owner;
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
