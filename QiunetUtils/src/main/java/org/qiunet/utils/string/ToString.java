package org.qiunet.utils.string;

import org.qiunet.utils.logger.LoggerType;
import org.qiunet.utils.reflect.ReflectUtil;

import java.lang.reflect.Modifier;
import java.util.*;

/***
 * 对对象进行toString
 *
 * @author qiunet
 * 2021/10/21 15:52
 **/
public final class ToString {

	/**
	 * The {@code null} text {@code '&lt;null&gt;'}.
	 */
	private static final String nullText = "<null>";
	/**
	 * The content start {@code '['}.
	 */
	private static final String contentStart = "[";

	/**
	 * The content end {@code ']'}.
	 */
	private static final String contentEnd = "]";

	/**
	 * The field name value separator {@code '='}.
	 */
	private static final String fieldNameValueSeparator = " = ";
	/**
	 * The field separator {@code ','}.
	 */
	private static final String fieldSeparator = ", ";

	/**
	 * The array start <code>'{'</code>.
	 */
	private static final String arrayStart = "{";

	/**
	 * The array separator {@code ','}.
	 */
	private static final String arraySeparator = ", ";


	/**
	 * The array end {@code '}'}.
	 */
	private static final String arrayEnd = "}";



	public static String toString(Object obj) {
		if (obj instanceof IDataToString) {
			return ((IDataToString) obj)._toString();
		}
		return ToString.objToString(obj);
	}

	/**
	 * 拼加字段
	 * @param obj
	 * @param joiner
	 */
	private static void appendFields(Object obj, StringJoiner joiner) {
		ReflectUtil.doWithFields(obj.getClass(), field -> {
			if (Modifier.isStatic(field.getModifiers())
					|| Modifier.isTransient(field.getModifiers())
			) {
				return;
			}

			try {
				field.setAccessible(true);
				final Object fieldValue = field.get(obj);
				joiner.add(field.getName() + fieldNameValueSeparator + objToString(fieldValue));
			} catch (IllegalAccessException e) {
				LoggerType.DUODUO.error("Object {} field {} get value error! {}", obj.getClass(), field.getName(), e);
			}
		});
	}

	/**
	 * 数组转string
	 * @param obj
	 * @return
	 */
	private static String arrToString(Object obj) {
		if (Objects.isNull(obj)) {
			return nullText;
		}

		Class<?> aClass = obj.getClass();
		StringJoiner joiner = new StringJoiner(arraySeparator, aClass.getSimpleName()+arrayStart, arrayEnd);
			if (aClass == int[].class) {
				for (int val : ((int[]) obj)) {
					joiner.add(String.valueOf(val));
				}
			} else if( aClass == long[].class) {
				for (long val : ((long[]) obj)) {
					joiner.add(String.valueOf(val));
				}
			}else if (aClass == byte[].class){
				for (byte val : ((byte[]) obj)) {
					joiner.add(String.valueOf(val));
				}
			}else if ( aClass == short[].class){
				for (short val : ((short[]) obj)) {
					joiner.add(String.valueOf(val));
				}
			}else if ( aClass == float[].class){
				for (float val : ((float[]) obj)) {
					joiner.add(String.valueOf(val));
				}
			}else if ( aClass == double[].class){
				for (double val : ((double[]) obj)) {
					joiner.add(String.valueOf(val));
				}
			}else if ( aClass == boolean[].class) {
				for (boolean val : ((boolean[]) obj)) {
					joiner.add(String.valueOf(val));
				}
			}else {
				for (Object o : ((Object[]) obj)) {
					joiner.add(objToString(o));
				}
			}
		return joiner.toString();
	}

	/**
	 * 拼加对象字段
	 * @param obj
	 * @return
	 */
	private static String objToString(Object obj) {
		if (obj == null) {
			return nullText;
		}

		Class<?> aClass = obj.getClass();
		if (aClass.isArray()) {
			return arrToString(obj);
		}

		if (aClass == String.class || aClass == Integer.class
		 || aClass == Long.class || aClass == Boolean.class
		 || aClass == Short.class || aClass == Float.class
		 || aClass == Byte.class || aClass == Double.class
		// 协议中. j protobuf 可能使用的反射生成的子类.
		 || aClass.isEnum() || aClass.getSuperclass().isEnum()
		) {
			return String.valueOf(obj);
		}

		if (Date.class.isAssignableFrom(aClass)) {
			return String.valueOf(((Date) obj).getTime());
		}

		if (Map.class.isAssignableFrom(aClass)) {
			StringJoiner joiner = new StringJoiner(arraySeparator, arrayStart, arrayEnd);
			((Map<?, ?>) obj).forEach((key, val) -> joiner.add(objToString(key) + fieldNameValueSeparator + objToString(val)));
			return joiner.toString();
		}

		if (Collection.class.isAssignableFrom(aClass)) {
			StringJoiner joiner = new StringJoiner(arraySeparator, arrayStart, arrayEnd);
			((Collection<?>) obj).forEach(val -> joiner.add(objToString(val)));
			return joiner.toString();
		}

		return ToString.objDetail(obj);
	}

	/**
	 * 处理对象
	 * @param obj
	 * @return
	 */
	private static String objDetail(Object obj) {
		if (Objects.isNull(obj)) {
			return nullText;
		}

		StringJoiner joiner = new StringJoiner(fieldSeparator, obj.getClass().getSimpleName() + contentStart, contentEnd);
		appendFields(obj, joiner);
		return joiner.toString();
	}

}
