package org.qiunet.function.reward;

import org.qiunet.cfg.convert.BaseObjConvert;

import java.lang.reflect.Field;

/***
 * rewards 转换类
 *
 * @Author qiunet
 * @Date 2020/12/29 08:04
 **/
public class RewardConvert extends BaseObjConvert<Rewards> {

	@Override
	public Rewards fromString(Field field, String str) {
		return RewardManager.instance.createRewards(str);
	}
}
