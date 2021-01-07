package org.qiunet.function.reward;

import org.qiunet.function.base.IResourceSubType;

/***
 * 真实奖励.
 * 普通奖励就是BaseReward本身. 装备奖励是装备实例
 *
 * @author qiunet
 * 2021-01-07 15:42
 */
public interface IRealReward {
	/**
	 * 奖励的子类型
	 * @param <T> ResourceSubType
	 * @return
	 */
	<T extends IResourceSubType> T subType();
}
