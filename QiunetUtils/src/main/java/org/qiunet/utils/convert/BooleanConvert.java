package org.qiunet.utils.convert;

import java.lang.reflect.Field;

/***
 *
 * @author qiunet
 * 2020-02-04 12:54
 **/
public class BooleanConvert extends BaseObjConvert<Boolean> {
	@Override
	public Boolean fromString(Field field, String str) {
		return "1".equals(str) || Boolean.parseBoolean(str);
	}

	@Override
	public boolean canConvert(Class<?> type) {
		return type == Boolean.class || type == Boolean.TYPE;
	}
}
