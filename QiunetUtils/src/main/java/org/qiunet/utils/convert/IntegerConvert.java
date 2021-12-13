package org.qiunet.utils.convert;

import org.qiunet.utils.string.StringUtil;

import java.lang.reflect.Field;

/***
 *
 * @author qiunet
 * 2020-02-04 12:54
 **/
public class IntegerConvert extends BaseObjConvert<Integer> {
	@Override
	public Integer fromString(Field field, String str) {
		if (StringUtil.isEmpty(str)) {
			return 0;
		}
		return Integer.valueOf(str);
	}

	@Override
	public boolean canConvert(Class<?> type) {
		return type == Integer.class || type == Integer.TYPE;
	}
}
