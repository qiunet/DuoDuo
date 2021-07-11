package org.qiunet.game.test.behavior.action;

/***
 * 行为执行者
 *  随机   选择   次序   并发  ROOT几种
 *
 * @author qiunet
 * 2021-07-08 11:09
 */
public interface IBehaviorExecutor extends IBehaviorNode {
	/**
	 * 执行器名称
	 * @return
	 */
	String getName();
}
