package org.qiunet.utils.convert;

import org.qiunet.utils.async.LazyLoader;
import org.qiunet.utils.exceptions.CustomException;
import org.qiunet.utils.string.StringUtil;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/***
 * 枚举的convert
 *
 * @author qiunet
 * 2020-02-08 10:30
 **/
public class EnumConvert extends BaseObjConvert<Enum> {
	private static final LazyLoader<Class<?>> enumReadableClass = new LazyLoader<>(() -> {
		try {
			return Class.forName("com.baidu.bjf.remoting.protobuf.EnumReadable");
		} catch (ClassNotFoundException e) {
			return null;
		}
	});
	@Override
	public Enum fromString(Field field, String value) {
		if (StringUtil.isEmpty(value)) {
			return null;
		}

		Class<Enum> enumClass = (Class<Enum>) (field.getType());
		if (StringUtil.isNum(value)) {
			// 有实现 EnumReadable 或者 IEnumReadable. 优先使用value. 否则使用 ordinal
			Integer val = Integer.valueOf(value);
			Enum[] enumConstants = enumClass.getEnumConstants();
			boolean enumReadable = IEnumReadable.class.isAssignableFrom(enumClass);
			if (! enumReadable) {
				Class<?> aClass = enumReadableClass.get();
				if (aClass != null && aClass.isAssignableFrom(enumClass)) {
					enumReadable = true;
				}
			}
			for (Enum enumConstant : enumConstants) {
				int enumValue = enumReadable ? getEnumValue(enumConstant) : enumConstant.ordinal();
				if (enumValue == val) {
					return enumConstant;
				}
			}
			return null;
		}
		return Enum.valueOf(enumClass, value);
	}

	private int getEnumValue(Enum enumConstant) {
		try {
			Method method = enumConstant.getDeclaringClass().getDeclaredMethod("value");
			return (int) method.invoke(enumConstant);
		} catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
			throw new CustomException(e, "convert enum {} error!", enumConstant.getDeclaringClass().getName());
		}
	}

	@Override
	public boolean canConvert(Class<?> type) {
		return type.isEnum() || Enum.class.isAssignableFrom(type);
	}
}
