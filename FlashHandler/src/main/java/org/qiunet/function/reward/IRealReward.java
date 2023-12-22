package org.qiunet.function.reward;

/***
 * 真实奖励. 方便客户端展示
 * 普通奖励就是BaseReward本身.
 * 装备奖励是装备实例
 *
 * @author qiunet
 * 2021-01-07 15:42
 */
public interface IRealReward {
	/**
	 * 有唯一id的给唯一id.
	 * 没有为 0
	 * @return
	 */
	int getUniqueId();
	/**
	 * 配置id
	 * @return
	 */
	int getCfgId();

	/**
	 * 获得数量信息
	 * @return
	 */
	long getCount();
}
