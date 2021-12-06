package org.qiunet.cfg.convert;


import org.qiunet.utils.exceptions.CustomException;
import sun.reflect.generics.reflectiveObjects.ParameterizedTypeImpl;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;

/***
 *
 * 配置文件里面 List 转对象的基类
 *
 * @author qiunet
 * 2020-02-04 12:13
 **/
public abstract class BaseObjListConvert<T> extends BaseObjConvert<List<T>> {
	private Class<T> clazz;

	public BaseObjListConvert() {
		Type[] actualTypeArguments = ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments();
		this.clazz = ((Class<T>) actualTypeArguments[0]);
	}

	@Override
	public boolean canConvert(Field field) {
		if (! (List.class.isAssignableFrom(field.getType()))) {
			return false;
		}

		Type genericType = field.getGenericType();
		if (! ParameterizedType.class.isAssignableFrom(genericType.getClass())){
			// 没有指定泛型
			return false;
		}

		return ((ParameterizedTypeImpl) genericType).getActualTypeArguments()[0] == clazz;
	}

	@Override
	public boolean canConvert(Class<?> type) {
		throw new CustomException("Not support");
	}
}
