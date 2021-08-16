package org.qiunet.game.test.behavior.node;

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
	 * 休眠毫秒数. 每个actionNode 里面执行的请求需要等待服务器下行.
	 */
	int EVERY_NODE_SLEEP_MS = 50;
	/**
	 * 执行器名称
	 * @return 名称
	 */
	String getName();
	/**
	 * 当前执行的行为节点
	 * @return
	 */
	IBehaviorNode currentBehavior();

	/**
	 * 移除某个节点
	 * 某些节点生命周期只需要执行一次. 比如登录.
	 * @param child 需要移除的节点
	 */
	void removeChild(IBehaviorNode child);

	/**
	 * 获得所有的节点 副本
	 * @return
	 */
	List<IBehaviorNode> getChildren();
}
