package org.qiunet.data.core.mongo.pojo;

import com.google.common.primitives.Primitives;
import org.bson.codecs.pojo.annotations.BsonIgnore;
import org.qiunet.utils.exceptions.CustomException;
import org.qiunet.utils.reflect.ReflectUtil;

import java.lang.reflect.*;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/***
 * Pojo 解析的Model
 *
 * @author qiunet
 * 2024/2/27 11:14
 ***/
class DClassModel<T> {
	/**
	 * 字段的属性相关
	 */
	private final Map<String, DPropertyModel<?>> propertyModels = new HashMap<>();
	/**
	 * 默认构造函数
	 */
	private final Constructor<T> defaultConstructor;
	/**
	 * 对应class
	 */
	private final Class<T> clz;


	DClassModel(Class<T> clz) {
		this.defaultConstructor = ReflectUtil.getMatchConstructor(clz);
		this.clz = clz;
		this.buildProperty(clz);
	}

	public T createInstance() {
		try {
			return this.defaultConstructor.newInstance();
		} catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
			throw new RuntimeException(e);
		}
	}

	private void buildProperty(Class<T> clz) {
		Class<?> c = clz;
		ParameterizedType parameter = null;
		while (c != Object.class) {
			Field[] fields = c.getDeclaredFields();
			for (Field field : fields) {
				if (field.isAnnotationPresent(BsonIgnore.class)) {
					continue;
				}

				if (Modifier.isStatic(field.getModifiers())
					|| Modifier.isTransient(field.getModifiers())
					|| Modifier.isFinal(field.getModifiers())
				){
					this.checkFinalCollectionAndMap(field);
					continue;
				}

				if (Map.class.isAssignableFrom(field.getType())) {
					// map.key 部分类型不允许
					ParameterizedType genericType = (ParameterizedType) field.getGenericType();
					Class<?> argument = (Class<?>) genericType.getActualTypeArguments()[0];
					if (! argument.isEnum() &&
					String.class != argument
					& ! Primitives.isWrapperType(argument)) {
						throw new CustomException("Map.key must be enum  string or primitive box type!");
					}
				}

				DPropertyModel<Object> model = new DPropertyModel<>(c, parameter, field);
				this.propertyModels.put(model.getDbFieldName(), model);
			}
			Type type = c.getGenericSuperclass();
			if (ParameterizedType.class.isAssignableFrom(type.getClass())) {
				parameter = (ParameterizedType) type;
			}else {
				parameter = null;
			}
			c = c.getSuperclass();
		}
	}
	private void checkFinalCollectionAndMap(Field field) {
		if (Modifier.isStatic(field.getModifiers())
			|| Modifier.isTransient(field.getModifiers())) {
			return;
		}

		if (Collection.class.isAssignableFrom(field.getType())) {
			throw new CustomException("Collection type ["+field.getName()+"] can not be final in class ["+ clz.getName()+"]!");
		}
		if (Map.class.isAssignableFrom(field.getType())) {
			throw new CustomException("Map type ["+field.getName()+"] can not be final in class ["+ clz.getName()+"]!");
		}
	}


	public DPropertyModel<?> getProperty(String dbFieldName) {
		return propertyModels.get(dbFieldName);
	}

	public Collection<DPropertyModel<?>> getPropertyModels() {
		return propertyModels.values();
	}

	public Class<T> getClz() {
		return clz;
	}
}
