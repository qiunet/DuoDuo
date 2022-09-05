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
	 *
	 * @param obj
	 * @return
	 */
	public static Map<String, String> toMap(Object obj) {
		Map<String, String> map = new HashMap<>();
		for (Field field : obj.getClass().getDeclaredFields()) {
			if (Modifier.isStatic(field.getModifiers())
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
			}
			if (val instanceof Date) {
				map.put(field.getName(), String.valueOf(((Date) val).getTime()));
			}

			if (Collection.class.isAssignableFrom(field.getType())) {
				map.put(field.getName(), JsonUtil.toJsonString(val));
			}
		}
		return map;
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
			}else if(Collection.class.isAssignableFrom(field.getType())){
				ReflectUtil.setField(t, field, JsonUtil.getGeneralObj(val, field.getGenericType()));
			}
		});
		return t;
	}
}
