package org.qiunet.data.util;

import org.qiunet.data.redis.support.info.IRedisEntity;
import org.qiunet.utils.date.DateUtil;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/***
 * Qiunet Data 里面操作交互数据的工具类
 * @Author qiunet
 * @Date Create in 2018/10/31 10:22
 **/
public class DataUtil {
	/**
	 * 获取某个对象某些字段的Map
	 * @param obj
	 * @param fields
	 * @return
	 */
	public static <T extends IRedisEntity> Map<String,String> getMap(T obj, String... fields){
		if (obj == null || fields == null || fields.length == 0) {
			throw new NullPointerException("obj ["+ (obj == null ? null : obj.getClass().getName())+"] fields ["+(fields == null ? null : Arrays.toString(fields))+"] Error");
		}

		Map<String,String> map = new HashMap<>();
		for(String fieldName : fields){
			try {
				Field field = getDeclaredField(obj, fieldName);
				if (field == null) continue;

				field.setAccessible(true);
				map.put(field.getName(), getFieldsValueStr(obj,field));
			}catch (Exception e) {
				e.printStackTrace();
			}
		}
		if(fields.length != map.size()){
			throw new NullPointerException("fields length is not equals, PARAMS"+Arrays.toString(fields) +" JUST"+Arrays.toString(map.keySet().toArray()));
		}
		return map;
	}

	/**
	 * 取到object 的所有Fields
	 * @param obj
	 * @return
	 */
	private static Map<Class<? extends IRedisEntity>, Map<String, Field>> fieldsCache = new HashMap<>(); // 缓存fields
	private static <T extends IRedisEntity>  Field getDeclaredField(T object, String fieldName){
		return getFieldsByClass(object.getClass()).get(fieldName);
	}

	private static Field[] combine(Field[] a, Field[] b){
		if(a==null) return b;
		if(b==null) return a;

		Field[] rt = new Field[a.length+b.length];
		System.arraycopy(a, 0, rt, 0, a.length);
		System.arraycopy(b, 0, rt, a.length, b.length);
		return rt;
	}

	/**
	 * 得到字段的值
	 * @param obj
	 * @param field
	 * @return
	 */
	private static String getFieldsValueStr(Object obj,Field field) throws IllegalAccessException {
		Object o = field.get(obj);

		if(o == null) throw new NullPointerException(field.getName() +" mapping fields is null~");

		if(o instanceof Date) return DateUtil.dateToString((Date)o);
		return o.toString();
	}
	/**
	 * 把map 的数据还原成对象
	 * @param map
	 * @param obj
	 * @param <T>
	 * @return
	 * @throws Exception
	 */
	public static <T extends IRedisEntity> T getObjFromMap(Map<String,String> map, T obj) {
		try {
			for (String key : map.keySet()) {
				Field field = getDeclaredField(obj, key);
				if (field == null) continue;
				// 根据使用的习惯. int String Date是绝大多数情况的使用顺序

				Method method = getSetMethod(obj, key, field.getType());
				if (field.getType() == int.class || field.getType() == Integer.class) {
					method.invoke(obj, Integer.parseInt(map.get(key)));
				} else if (field.getType() == String.class) {
					method.invoke(obj, map.get(key));
				} else if (field.getType() == Date.class) {
					method.invoke(obj, DateUtil.stringToDate(map.get(key)));
				} else if (field.getType() == long.class || field.getType() == Long.class) {
					method.invoke(obj, Long.parseLong(map.get(key)));
				} else if (field.getType() == boolean.class || field.getType() == Boolean.class) {
					method.invoke(obj, Boolean.parseBoolean(map.get(key)));
				} else if (field.getType() == float.class || field.getType() == Float.class) {
					method.invoke(obj, Float.parseFloat(map.get(key)));
				} else if (field.getType() == double.class || field.getType() == Double.class) {
					method.invoke(obj, Double.parseDouble(map.get(key)));
				} else if (field.getType() == byte.class || field.getType() == Byte.class) {
					method.invoke(obj, Byte.parseByte(map.get(key)));
				} else if (field.getType() == short.class || field.getType() == Short.class) {
					method.invoke(obj, Short.parseShort(map.get(key)));
				}
			}
			return obj;
		}catch (Exception e) {
			e.printStackTrace();
		}
		return  null;
	}
	public static Method getSetMethod(Object object,String fieldName,Class<?> fieldType) throws NoSuchMethodException {
		char [] chars = ("set"+fieldName).toCharArray();
		chars[3] -= 32;
		String methodName = new String(chars);

		Class<?> clazz=object.getClass();
		Method method = clazz.getMethod(methodName, fieldType);
		return method;
	}

	/***
	 * 得到某个class的所有fields 以map方式返回.
	 *
	 * @param clazz
	 * @return
	 */
	public static Map<String, Field> getFieldsByClass(Class<? extends IRedisEntity> clazz) {
		if (!fieldsCache.containsKey(clazz)) {
			Field[] rt=null;
			Class sClazz = clazz;
			for(;sClazz!=Object.class;sClazz=sClazz.getSuperclass()){
				Field[] tmp = sClazz.getDeclaredFields();
				rt = combine(rt, tmp);
			}
			Map<String, Field> fieldMap = new HashMap<>();
			for (Field f : rt)  {
				if (Modifier.isFinal(f.getModifiers()) ) continue;

				fieldMap.put(f.getName(), f);
			}

			fieldsCache.put(clazz, fieldMap);
		}
		return fieldsCache.get(clazz);
	}
}
