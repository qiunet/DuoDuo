package org.qiunet.cfg.convert;

import org.qiunet.utils.string.StringUtil;

import java.lang.reflect.Field;

/***
 *
 * @author qiunet
 * 2020-02-04 12:54
 **/
public class ByteConvert extends BaseObjConvert<Byte> {
	@Override
	public Byte fromString(Field field, String str) {
		if (StringUtil.isEmpty(str)) {
			return 0;
		}
		return Byte.valueOf(str);
	}

	@Override
	public boolean canConvert(Class<Byte> type) {
		return type == Byte.class || type == Byte.TYPE;
	}
}
