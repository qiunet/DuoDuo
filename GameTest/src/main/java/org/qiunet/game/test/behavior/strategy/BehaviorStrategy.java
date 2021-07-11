package org.qiunet.game.test.behavior.strategy;

/***
 * 行为执行策略.
 *
 * @author qiunet
 * 2021-07-05 10:00
 */
public enum BehaviorStrategy {
	/**
	 * 随机一定数量的节点执行
	 */
	RANDOM,
	/**
	 * 顺序执行
	 * 如果有返回true .则停止执行
	 */
	SELECTOR,
	/**
	 *  顺序执行
	 *  如果返回false. 则停止执行
	 */
	SEQUENCE,
	/**
	 * 同时运行所有行为节点
	 */
	PARALLEL,
}
