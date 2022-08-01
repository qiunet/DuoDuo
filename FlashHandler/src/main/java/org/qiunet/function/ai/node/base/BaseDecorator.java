package org.qiunet.function.ai.node.base;

import com.google.common.collect.ImmutableList;
import org.qiunet.flash.handler.common.MessageHandler;
import org.qiunet.function.ai.node.IBehaviorDecorator;
import org.qiunet.function.ai.node.IBehaviorExecutor;
import org.qiunet.function.ai.node.IBehaviorNode;
import org.qiunet.utils.exceptions.CustomException;
import org.qiunet.utils.reflect.ReflectUtil;

import java.util.List;

/***
 * 装饰节点父类
 *
 * qiunet
 * 2021/8/16 21:38
 **/
public abstract class BaseDecorator<Owner extends MessageHandler<Owner>> extends BaseBehaviorNode<Owner> implements IBehaviorDecorator<Owner> {
	/**
	 * 需要翻转的节点
	 */
	protected IBehaviorNode<Owner> node;
	private List<IBehaviorNode<Owner>> nodes;

	public BaseDecorator(String name) {
		super(null, name);
	}

	public BaseDecorator(IBehaviorNode<Owner> node, String name) {
		super(null, name);
		this.addChild0(node);
	}

	@Override
	public IBehaviorExecutor<Owner> addChild(IBehaviorNode<Owner>... actions) {
		return this.addChild0(actions);
	}


	public IBehaviorExecutor<Owner> addChild0(IBehaviorNode<Owner>... actions) {
		if (this.node != null || actions.length > 1) {
			throw new CustomException("Not support multi node in decorator node!");
		}

		if (actions.length == 0) {
			throw new NullPointerException();
		}

		IBehaviorNode<Owner> node = actions[0];
		ReflectUtil.setField(node, "parent", this);
		this.nodes = ImmutableList.of(node);
		this.node = node;
		return this;
	}

	@Override
	public List<IBehaviorNode<Owner>> getChildNodes() {
		return this.nodes;
	}

	@Override
	public IBehaviorNode<Owner> getNode() {
		return node;
	}

	@Override
	public void prepare() {
		super.prepare();
		node.prepare();
	}

	@Override
	public void initialize() {
		super.initialize();
		node.initialize();
	}

	@Override
	public void reset() {
		super.reset();
		node.reset();
	}

	@Override
	public void check() {
		super.check();
		node.check();
	}

	@Override
	public void release() {
		super.release();
		node.release();
	}

	@Override
	public boolean preCondition() {
		if (node.isRunning()) {
			return true;
		}
		return node.preCondition();
	}
}
