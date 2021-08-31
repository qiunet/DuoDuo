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
public interface IBehaviorExecutor extends IBehaviorNode {
	/**
	 * 移除某个节点
	 * 某些节点生命周期只需要执行一次. 比如登录.
	 * @param child 需要移除的节点
	 */
	void removeChild(IBehaviorNode child);

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
	List<IBehaviorNode> getChildNodes();
	/**
	 *  子节点数
	 * @return
	 */
	int childSize();

	/**
	 * 控制器里面. 前面的action节点.可能会导致后面节点条件检查变动. 所以条件判断交给node去做.
	 * @return
	 */
	@Override
	default boolean preCondition() {
		return true;
	}
}
