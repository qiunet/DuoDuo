package org.qiunet.cfg.convert;

import com.baidu.bjf.remoting.protobuf.EnumReadable;
import org.qiunet.utils.string.StringUtil;

import java.lang.reflect.Field;

/***
 * 枚举的convert
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

		Class<Enum> enumClass = (Class<Enum>) (field.getType());
		if (StringUtil.isNum(value)) {
			// 有实现 EnumReadable. 优先使用value. 否则使用 ordinal
			Integer val = Integer.valueOf(value);
			Enum[] enumConstants = enumClass.getEnumConstants();
			boolean enumReadable = EnumReadable.class.isAssignableFrom(enumClass);
			for (Enum enumConstant : enumConstants) {
				int enumValue = enumReadable ? ((EnumReadable) enumConstant).value() : enumConstant.ordinal();
				if (enumValue == val) {
					return enumConstant;
				}
			}
			return null;
		}
		return Enum.valueOf(enumClass, value);
	}

	@Override
	public boolean canConvert(Class<?> type) {
		return type.isEnum() || Enum.class.isAssignableFrom(type);
	}
}
