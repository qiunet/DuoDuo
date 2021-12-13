package org.qiunet.utils.convert;

import org.qiunet.utils.string.StringUtil;

import java.lang.reflect.Field;

/***
 *
 * @author qiunet
 * 2021/11/12 11:03
 */
public class FloatConvert extends BaseObjConvert<Float> {

	@Override
	public Float fromString(Field field, String str) {
		if (StringUtil.isEmpty(str)) {
			return 0F;
		}
		return Float.parseFloat(str);
	}

	@Override
	public boolean canConvert(Class<?> type) {
		return type == float.class || type == Float.class;
	}
}
