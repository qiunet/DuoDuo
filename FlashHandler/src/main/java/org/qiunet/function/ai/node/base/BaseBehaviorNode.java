package org.qiunet.function.ai.node.base;

import org.qiunet.flash.handler.common.MessageHandler;
import org.qiunet.function.ai.enums.ActionStatus;
import org.qiunet.function.ai.log.BHTStatusLogger;
import org.qiunet.function.ai.node.IBehaviorExecutor;
import org.qiunet.function.ai.node.IBehaviorNode;
import org.qiunet.function.ai.node.root.BehaviorRootTree;
import org.qiunet.function.condition.IConditions;
import org.qiunet.utils.async.LazyLoader;

/***
 *  节点类型
 *
 * @author qiunet
 * 2021-07-05 11:49
 */
public abstract class BaseBehaviorNode<Owner extends MessageHandler<Owner>> implements IBehaviorNode<Owner> {
	protected final LazyLoader<BehaviorRootTree<Owner>> rootNode = new LazyLoader<>(() -> this.parent().rootNode());
	private final LazyLoader<Integer> id = new LazyLoader<>(() -> rootNode().generatorId());
	/**
	 * 父节点
	 */
	protected IBehaviorExecutor<Owner> parent;
	/**
	 * 状态记录
	 */
	protected BHTStatusLogger<Owner> statusLogger;
	/**
	 * 节点名称
	 * 读取Ai.xml设置.
	 * 自己手写ai逻辑没有
	 */
	private String name;

	/**
	 * 节点执行条件
	 */
	private final IConditions<Owner> conditions;

	public BaseBehaviorNode(IConditions<Owner> conditions, String name) {
		this.statusLogger = new BHTStatusLogger<>(this);
		this.conditions = conditions;
		this.name = name;
	}

	@Override
	public boolean isRunning() {
		return statusLogger.getStatus() != null && statusLogger.getStatus().isRunning();
	}

	@Override
	public BHTStatusLogger<Owner> statusLogger() {
		return statusLogger;
	}

	@Override
	public IBehaviorExecutor<Owner> parent() {
		return parent;
	}

	@Override
	public BehaviorRootTree<Owner> rootNode() {
		if (parent == null) {
			return null;
		}
		return rootNode.get();
	}

	@Override
	public ActionStatus run() {
		ActionStatus status = execute();
		this.statusLogger.setExecuted(true);
		this.statusLogger.setStatus(status);
		return status;
	}

	@Override
	public int getId() {
		return id.get();
	}

	@Override
	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public boolean preCondition() {
		if (conditions == null) {
			return true;
		}
		return conditions.verify(this.getOwner()).isSuccess();
	}

	/**
	 * 具体的执行逻辑
	 * @return
	 */
	protected abstract ActionStatus execute();
}
