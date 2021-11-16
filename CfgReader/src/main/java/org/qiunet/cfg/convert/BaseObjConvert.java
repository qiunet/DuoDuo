package org.qiunet.cfg.convert;


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
		clazz = (Class<T>) actualTypeArguments[0];
	}

	/***
	 * 转换成自己需要的对象
	 * @param str
	 * @return
	 */
	public abstract T fromString(Field field, String str);

	/**
	 * 是否可以转换
	 * @param type
	 * @return
	 */
	public boolean canConvert(Class<T> type) {
		return type == clazz;
	}
}
