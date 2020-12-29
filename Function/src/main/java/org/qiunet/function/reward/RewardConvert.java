package org.qiunet.function.reward;

import org.qiunet.cfg.convert.BaseObjConvert;

/***
 * rewards 转换类
 *
 * @Author qiunet
 * @Date 2020/12/29 08:04
 **/
public class RewardConvert extends BaseObjConvert<Rewards> {

	@Override
	protected Rewards fromString0(String str) {
		return RewardManager.instance.createRewards(str);
	}

	@Override
	public boolean canConvert(Class type) {
		return type == Rewards.class;
	}
}
