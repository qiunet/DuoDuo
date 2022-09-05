package org.qiunet.function.reward;

import com.alibaba.fastjson.TypeReference;
import org.qiunet.utils.convert.BaseObjConvert;
import org.qiunet.utils.json.JsonUtil;
import org.qiunet.utils.string.StringUtil;

import java.lang.reflect.Field;
import java.util.Collections;
import java.util.List;

/***
 * rewards 转换类
 *
 * @Author qiunet
 * @Date 2020/12/29 08:04
 **/
public class RewardConvert extends BaseObjConvert<Rewards> {
	private static final TypeReference<List<RewardConfig>> CONFIG_JSON_TYPE = new TypeReference<List<RewardConfig>>(){};
	@Override
	public Rewards fromString(Field field, String str) {
		List<RewardConfig> rewardConfigs = Collections.emptyList();
		if (!StringUtil.isEmpty(str)) {
			rewardConfigs = JsonUtil.getGeneralObj(str, CONFIG_JSON_TYPE);
		}
		return RewardManager.instance.createRewards(rewardConfigs);
	}
}
