package org.qiunet.function.reward;

import org.qiunet.function.base.IResourceCfg;
import org.qiunet.function.base.basic.BasicFunctionManager;

/***
 * 真实奖励.
 * 普通奖励就是BaseReward本身. 装备奖励是装备实例
 *
 * @author qiunet
 * 2021-01-07 15:42
 */
public interface IRealReward {
	/**
	 * 配置id
	 * @return
	 */
	int getCfgId();

	/**
	 * 获得资源配置
	 * @param <T>
	 * @return
	 */
	default <T extends IResourceCfg> T getResourceCfg() {
		return BasicFunctionManager.instance.getResById(getCfgId());
	}
}
