package org.qiunet.function.condition;

import com.alibaba.fastjson.TypeReference;
import org.qiunet.cfg.convert.BaseObjConvert;
import org.qiunet.utils.json.JsonUtil;
import org.qiunet.utils.string.StringUtil;

import java.lang.reflect.Field;
import java.util.List;

/***
 * 条件转换器. 支持 or 转换 使用 "||" 对两个数组操作.
 *
 * @author qiunet
 * 2020-12-31 15:31
 */
public class ConditionConvert extends BaseObjConvert<IConditions> {
	private static final TypeReference<List<ConditionConfig>> TYPE = new TypeReference<List<ConditionConfig>>(){};

	@Override
	public IConditions fromString(Field field, String str) {
		if (StringUtil.isEmpty(str)) {
			return ConditionManager.EMPTY_CONDITION;
		} else if (str.contains("||")) {
			String[] splits = StringUtil.split(str, "||");
			IConditions conditions = convert(splits[0]);
			if (splits.length > 1) {
				for (int i = 1; i < splits.length; i++) {
					conditions = conditions.or(convert(splits[i]));
				}
			}
			return conditions;
		} else {
			return convert(str);
		}
	}

	private IConditions convert(String str) {
		List<ConditionConfig> configList = JsonUtil.getGeneralObjWithField(str.trim(), TYPE);
		return ConditionManager.createCondition(configList);
	}

	@Override
	public boolean canConvert(Class<?> aClass) {
		return aClass.isAssignableFrom(IConditions.class);
	}
}
