package org.qiunet.utils.convert;

import org.qiunet.utils.string.StringUtil;

import java.lang.reflect.Field;

/***
 *
 * @author qiunet
 * 2020-02-04 12:54
 **/
public class LongConvert extends BaseObjConvert<Long> {
	@Override
	public Long fromString(Field field, String str) {
		if (StringUtil.isEmpty(str)) {
			return 0L;
		}
		return Long.valueOf(str);
	}

	@Override
	public boolean canConvert(Class<?> type) {
		return type == Long.class || type == Long.TYPE;
	}
}
