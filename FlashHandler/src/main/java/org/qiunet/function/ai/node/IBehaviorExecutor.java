package org.qiunet.function.ai.node;

import org.qiunet.utils.exceptions.CustomException;

import java.util.List;

/***
 * 行为执行者
 *  随机   选择   次序   并发  ROOT几种
 *
 * @author qiunet
 * 2021-07-08 11:09
 */
public interface IBehaviorExecutor<Owner> extends IBehaviorNode<Owner> {
	/**
	 * 添加节点
	 * @param actions
	 * @return
	 */
	IBehaviorExecutor<Owner> addChild(IBehaviorNode<Owner>... actions);
	/**
	 * 移除某个节点
	 * 某些节点生命周期只需要执行一次. 比如登录.
	 * @param child 需要移除的节点
	 */
	void removeChild(IBehaviorNode<Owner> child);

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
	/**
	 *  子节点数
	 * @return
	 */
	int childSize();
}
