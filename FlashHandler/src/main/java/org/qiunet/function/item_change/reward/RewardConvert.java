package org.qiunet.function.item_change.reward;

import com.alibaba.fastjson2.TypeReference;
import org.qiunet.function.item_change.ItemChangeConfig;
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
	private static final TypeReference<List<ItemChangeConfig>> CONFIG_JSON_TYPE = new TypeReference<List<ItemChangeConfig>>(){};
	@Override
	public Rewards fromString(Field field, String str) {
		List<ItemChangeConfig> itemChangeConfigs = Collections.emptyList();
		if (!StringUtil.isEmpty(str)) {
			itemChangeConfigs = JsonUtil.getGeneralObj(str, CONFIG_JSON_TYPE);
		}
		return RewardManager.instance.createRewards(itemChangeConfigs);
	}
}
