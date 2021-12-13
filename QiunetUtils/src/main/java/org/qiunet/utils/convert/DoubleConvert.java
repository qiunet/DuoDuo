package org.qiunet.utils.convert;

import org.qiunet.utils.string.StringUtil;

import java.lang.reflect.Field;

/***
 *
 * @author qiunet
 * 2020-02-04 14:57
 **/
public class DoubleConvert extends BaseObjConvert<Double> {
	@Override
	public Double fromString(Field field, String str) {
		if (StringUtil.isEmpty(str)) {
			return 0d;
		}
		return Double.valueOf(str);
	}

	@Override
	public boolean canConvert(Class<?> type) {
		return type == Double.TYPE || type == Double.class;
	}
}
