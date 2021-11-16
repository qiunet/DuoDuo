package org.qiunet.cfg.convert;

import java.lang.reflect.Field;

/***
 *
 * @author qiunet
 * 2020-02-04 12:54
 **/
public class ShortConvert extends BaseObjConvert<Short> {
	@Override
	public Short fromString(Field field, String str) {
		return Short.valueOf(str);
	}

	@Override
	public boolean canConvert(Class<Short> type) {
		return type == Short.class || type == Short.TYPE;
	}
}
