package org.qiunet.function.condition;

import org.qiunet.cfg.convert.BaseObjConvert;

import java.lang.reflect.Field;

/***
 * 条件转换器. 支持 or 转换 使用 "||" 对两个数组操作.
 *
 * @author qiunet
 * 2020-12-31 15:31
 */
public class ConditionConvert extends BaseObjConvert<IConditions> {


	@Override
	public IConditions fromString(Field field, String str) {
		return ConditionManager.createCondition(str);
	}

	@Override
	public boolean canConvert(Class<?> aClass) {
		return aClass.isAssignableFrom(IConditions.class);
	}
}
