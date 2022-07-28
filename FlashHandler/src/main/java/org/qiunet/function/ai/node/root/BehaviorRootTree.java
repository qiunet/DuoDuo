package org.qiunet.function.ai.node.root;

import org.qiunet.function.ai.enums.ActionStatus;
import org.qiunet.function.ai.node.IBehaviorExecutor;
import org.qiunet.function.ai.node.IBehaviorNode;
import org.qiunet.function.ai.node.base.BaseDecorator;
import org.qiunet.function.ai.node.executor.WheelExecutor;
import org.qiunet.utils.exceptions.CustomException;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

/***
 *  执行器ROOT
 *
 * qiunet
 * 2021/7/26 09:37
 **/
public final class BehaviorRootTree<Owner> extends BaseDecorator<Owner> {
	/**
	 * 准备工作.
	 * 只执行一次
	 */
	private final AtomicBoolean prepared = new AtomicBoolean(false);
	/**
	 * id 分配
	 */
	private final AtomicInteger idMaker = new AtomicInteger();
	/**
	 * 属主
	 */
	private final Owner owner;
	/**
	 * 默认使用 selector 节点作为root节点
	 */
	public BehaviorRootTree(Owner owner){
		super(new WheelExecutor<>(null, "Root"), "Root");
		this.owner = owner;
	}

	public void tick(){
		if (! prepared.get() && prepared.compareAndSet(false, true)) {
			node.prepare();
		}

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

	/**
	 * 分配一个ID
	 * @return
	 */
	public int generatorId(){
		return idMaker.incrementAndGet();
	}

	@Override
	public BehaviorRootTree<Owner> rootNode() {
		return this;
	}

	@Override
	public int getId() {
		// root 的id为0
		return 0;
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
