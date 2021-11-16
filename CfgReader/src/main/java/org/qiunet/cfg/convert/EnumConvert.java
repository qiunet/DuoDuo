package org.qiunet.cfg.convert;

import org.qiunet.utils.string.StringUtil;

import java.lang.reflect.Field;

/***
 *
 * @author qiunet
 * 2020-02-08 10:30
 **/
public class EnumConvert extends BaseObjConvert<Enum> {
	@Override
	public Enum fromString(Field field, String value) {
		if (StringUtil.isEmpty(value)) {
			return null;
		}

		return Enum.valueOf((Class<Enum>)(field.getType()), value);
	}

	@Override
	public boolean canConvert(Class<Enum> type) {
		return type.isEnum() || Enum.class.isAssignableFrom(type);
	}
}
