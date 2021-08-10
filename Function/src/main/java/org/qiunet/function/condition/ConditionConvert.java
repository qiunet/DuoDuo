package org.qiunet.function.condition;

import com.alibaba.fastjson.TypeReference;
import org.qiunet.cfg.convert.BaseObjConvert;
import org.qiunet.utils.json.JsonUtil;
import org.qiunet.utils.string.StringUtil;

import java.util.List;

/***
 *
 *
 * @author qiunet
 * 2020-12-31 15:31
 */
public class ConditionConvert extends BaseObjConvert<IConditionData> {
	private static final TypeReference<List<ConditionConfig>> TYPE = new TypeReference<List<ConditionConfig>>(){};

	@Override
	protected IConditionData fromString0(String str) {
		if (StringUtil.isEmpty(str)) {
			return ConditionManager.EMPTY_CONDITION;
		} else {
			List<ConditionConfig> configList = JsonUtil.getGeneralObjWithField(str, TYPE);
			return ConditionManager.createCondition(configList);
		}
	}

	@Override
	public boolean canConvert(Class aClass) {
		return aClass.isAssignableFrom(IConditionData.class);
	}
}
