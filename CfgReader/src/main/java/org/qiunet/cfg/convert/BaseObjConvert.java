package org.qiunet.cfg.convert;


import org.qiunet.utils.exceptions.CustomException;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/***
 *
 * 配置文件里面 string 转对象的基类
 * @author qiunet
 * 2020-02-04 12:13
 **/
public abstract class BaseObjConvert<T> {
	private Class<T> clazz;
	public BaseObjConvert() {
		Type[] actualTypeArguments = ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments();
		if (actualTypeArguments[0] instanceof Class) {
			this.clazz = ((Class<T>) actualTypeArguments[0]);
			// 其它泛型类型的. 由子类实现 canConvert(Field field) 判断是否可以转换.
		}
	}

	/***
	 * 转换成自己需要的对象
	 * @param str
	 * @return
	 */
	public abstract T fromString(Field field, String str);

	/**
	 * 是否可以转换
	 * 需要用泛型判断的. 需要重写该方法
	 * @param field
	 * @return
	 */
	public boolean canConvert(Field field) {
		Class<?> type = field.getType();
		return this.canConvert(type);
	}
	/**
	 * 是否可以转换
	 * @param type
	 * @return
	 */
	public boolean canConvert(Class<?> type) {
		if (clazz == null) {
			throw new CustomException("Generic class is absent! you need call canConvert(Field field)!");
		}
		return type == clazz;
	}
}
