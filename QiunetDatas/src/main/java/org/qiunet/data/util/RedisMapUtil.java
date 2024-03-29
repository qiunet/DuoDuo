package org.qiunet.data.util;

import org.qiunet.utils.json.JsonUtil;
import org.qiunet.utils.reflect.ReflectUtil;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/***
 * 简单对象(只有一层 数据类型为string long int date.)
 * 转 Map{string, string}
 * @author qiunet
 * 2022/4/12 15:03
 */
public class RedisMapUtil {
	/**
	 * 将obj 转化为map{string, string}
	 * @param obj 需要转化的obj
	 * @return map
	 */
	public static Map<String, String> toMap(Object obj) {
		Map<String, String> map = new HashMap<>();
		toMap(obj, obj.getClass(), map);
		return map;
	}

	/**
	 * 将obj 转化为map{string, string}
	 * @param obj 需要转化的obj
	 * @return map
	 */
	private static void toMap(Object obj, Class<?> clz, Map<String, String> map) {
		if (clz.getSuperclass() != Object.class) {
			toMap(obj, clz.getSuperclass(), map);
		}

		for (Field field : clz.getDeclaredFields()) {
			if (Modifier.isStatic(field.getModifiers())
			|| Modifier.isTransient(field.getModifiers())
			|| Modifier.isFinal(field.getModifiers())) {
				continue;
			}

			Object val = ReflectUtil.getField(field, obj);
			if (val == null) {
				continue;
			}

			if (val.getClass() == String.class
				|| val.getClass() == Long.class
				|| val.getClass() == Integer.class) {
				map.put(field.getName(), String.valueOf(val));
			}else if (val instanceof Date dt) {
				map.put(field.getName(), String.valueOf(dt.getTime()));
			}else {
				map.put(field.getName(), JsonUtil.toJsonString(val));
			}
		}
	}

	/**
	 * huan
	 * @param data
	 * @param clz
	 * @param <T>
	 * @return
	 */
	public static <T> T toObj(Map<String, String> data, Class<T> clz) {
		T t = ReflectUtil.newInstance(clz);
		data.forEach((fieldName, val) -> {
			Field field = ReflectUtil.findField(clz, fieldName);
			if (field.getType() == String.class) {
				ReflectUtil.setField(t, field, val);
			}else if(field.getType() == Date.class){
				ReflectUtil.setField(t, field, new Date(Long.parseLong(val)));
			}else if(field.getType() == Integer.class || field.getType() == int.class){
				ReflectUtil.setField(t, field, Integer.parseInt(val));
			}else if(field.getType() == Long.class || field.getType() == long.class){
				ReflectUtil.setField(t, field, Long.parseLong(val));
			}else if(Collection.class.isAssignableFrom(field.getType())
			|| Map.class.isAssignableFrom(field.getType())){
				ReflectUtil.setField(t, field, JsonUtil.getGeneralObj(val, field.getGenericType()));
			}else {
				ReflectUtil.setField(t, field, JsonUtil.getGeneralObj(val, field.getType()));
			}
		});
		return t;
	}
}
