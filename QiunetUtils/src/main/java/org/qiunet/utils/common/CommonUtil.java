package org.qiunet.utils.common;

import org.qiunet.utils.date.DateUtil;
import org.qiunet.utils.logger.LoggerType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.*;

/**
 * 其它可以通用逻辑的工具类
 * @author qiunet
 *
 */
public class CommonUtil {
	private static Logger logger = LoggerFactory.getLogger(LoggerType.QIUNET_UTILS);

	private CommonUtil() { }
	/**
	 * 检查一个元素是否在数组中
	 * @param <T>
	 * @param list
	 * @return
	 */
	public static <T> boolean existInList(T element,T list[])
	{
		for(T t : list)
		{
			if(element.equals(t))
			{
				return true;
			}
		}
		return false;
	}
	/**
	 * 检查一个元素是否在集合中
	 * @param <T>
	 * @param element
	 * @param list
	 * @return
	 */
	public static <T> boolean existInList(T element,Collection<T> list)
	{
		if(list.isEmpty()) return false;
		for(T t : list)
		{
			if(element.equals(t))
			{
				return true;
			}
		}
		return false;
	}
	/**
	 * 截取某列表的部分数据
	 * @param list 原始list
	 * @param skip 从skip 开始
	 * @param count 截取数
	 * @return
	 */
	public static <T> List<T> getSubListPage(List<T> list, int skip , int count) {
		if(list==null||list.isEmpty()){
			return null;
		}
		int startIndex = skip;
		int endIndex = skip+count;
		if(startIndex>endIndex||startIndex>list.size()){
			return null;
		}
		if(endIndex>list.size()){
			endIndex = list.size();
		}
		return list.subList(startIndex, endIndex);
	}
	/**
	 * 获取某个对象某些字段的Map
	 * @param obj
	 * @param fields
	 * @return
	 */
	public static Map<String,String> getMap(Object obj, String... fields){
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
	private static Map<Class, Map<String, Field>> fieldsCache = new HashMap<>(); // 缓存fields
	private static Field getDeclaredField(Object object, String fieldName){
		Class<?> clazz = object.getClass();
		Class originClass = clazz;
		if (!fieldsCache.containsKey(originClass)) {
			Field[] rt=null;
			for(;clazz!=Object.class;clazz=clazz.getSuperclass()){
				Field[] tmp = clazz.getDeclaredFields();
				rt = combine(rt, tmp);
			}
			Map<String, Field> fieldMap = new HashMap<>();
			for (Field f : rt)  {
				if (Modifier.isFinal(f.getModifiers()) ) continue;

				fieldMap.put(f.getName(), f);
			}

			fieldsCache.put(originClass, fieldMap);
		}
		return fieldsCache.get(originClass).get(fieldName);
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
	 * 从map中构造一个对象
	 * @param map
	 * @param clazz
	 * @return
	 */
	public static <T> T getObjFromMap(Map<String,String> map,Class<?> clazz){
		try {
			Object obj=clazz.newInstance();
			return getObjFromMap(map, obj);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 把map 的数据还原成对象
	 * @param map
	 * @param obj
	 * @param <T>
	 * @return
	 * @throws Exception
	 */
	public static <T> T getObjFromMap(Map<String,String> map, Object obj) {
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
			return (T)obj;
		}catch (Exception e) {
			e.printStackTrace();
		}
		return  null;
	}
	private static Method getSetMethod(Object object,String fieldName,Class<?> fieldType) throws NoSuchMethodException {
		char [] chars = ("set"+fieldName).toCharArray();
		chars[3] -= 32;
		String methodName = new String(chars);

		Class<?> clazz=object.getClass();
		Method method = clazz.getMethod(methodName, fieldType);
		return method;
	}
}
