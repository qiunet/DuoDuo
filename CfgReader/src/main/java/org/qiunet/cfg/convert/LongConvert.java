package org.qiunet.cfg.convert;

import java.lang.reflect.Field;

/***
 *
 * @author qiunet
 * 2020-02-04 12:54
 **/
public class LongConvert extends BaseObjConvert<Long> {
	@Override
	public Long fromString(Field field, String str) {
		return Long.valueOf(str);
	}

	@Override
	public boolean canConvert(Class<Long> type) {
		return type == Long.class || type == Long.TYPE;
	}
}
