package org.qiunet.function.ai.node;

import org.qiunet.flash.handler.common.MessageHandler;
import org.qiunet.utils.exceptions.CustomException;

import java.util.List;

/***
 * 行为执行者
 *  随机   选择   次序   并发  ROOT几种
 *
 * @author qiunet
 * 2021-07-08 11:09
 */
public interface IBehaviorExecutor<Owner extends MessageHandler<Owner>> extends IBehaviorNode<Owner> {
	/**
	 * 添加节点
	 * @param actions
	 * @return
	 */
	IBehaviorExecutor<Owner> addChild(IBehaviorNode<Owner>... actions);

	@Override
	default void check() {
		if (getChildNodes().isEmpty()) {
			throw new CustomException("child nodes is empty!");
		}
		getChildNodes().forEach(IBehaviorNode::check);
	}

	/**
	 * 获得所有的node
	 * @return
	 */
	List<IBehaviorNode<Owner>> getChildNodes();
	@Override
	default void initialize() {
		IBehaviorNode.super.initialize();
		getChildNodes().forEach(IBehaviorNode::initialize);
	}

	@Override
	default void prepare() {
		getChildNodes().forEach(IBehaviorNode::prepare);
	}

	@Override
	default void reset() {
		getChildNodes().forEach(IBehaviorNode::reset);
	}

	@Override
	default void release() {
		getChildNodes().forEach(IBehaviorNode::release);
	}
	/**
	 *  子节点数
	 * @return
	 */
	int childSize();
}
