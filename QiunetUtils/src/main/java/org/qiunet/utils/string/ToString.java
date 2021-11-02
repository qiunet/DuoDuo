package org.qiunet.utils.string;

import org.apache.commons.lang3.ArraySorter;
import org.qiunet.utils.logger.LoggerType;

import java.lang.reflect.Field;
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

	private final Object obj;
	ToString(Object obj) {
		this.obj = obj;
	}

	public static String toString(Object obj) {
		return new ToString(obj).toString();
	}

	private void appendFields(StringJoiner joiner) {
		final Field[] fields = ArraySorter.sort(obj.getClass().getDeclaredFields(), Comparator.comparing(Field::getName));
		for (Field field : fields) {
			if (Modifier.isStatic(field.getModifiers())
			 || Modifier.isTransient(field.getModifiers())
			) {
				continue;
			}

			try {
				field.setAccessible(true);
				final Object fieldValue = field.get(obj);
				joiner.add(field.getName() + fieldNameValueSeparator + this.valToString(fieldValue));
			} catch (IllegalAccessException e) {
				LoggerType.DUODUO.error("Object {} field {} get value error! {}", obj.getClass(), field.getName(), e);
			}
		}
	}

	/**
	 *
	 * @param fieldValue
	 * @return
	 */
	private String valToString(Object fieldValue) {
		if (fieldValue == null) {
			return nullText;
		}

		Class<?> aClass = fieldValue.getClass();
		if (aClass == String.class || aClass == Integer.class
		 || aClass == Long.class || aClass == Boolean.class
		 || aClass == Short.class || aClass == Float.class
		 || aClass == Byte.class || aClass == Double.class
		) {
			return String.valueOf(fieldValue);
		}

		if (aClass.isEnum()) {
			return ((Enum<?>) fieldValue).name();
		}

		if (Date.class.isAssignableFrom(aClass)) {
			return String.valueOf(((Date) fieldValue).getTime());
		}

		if (Map.class.isAssignableFrom(aClass)) {
			StringJoiner joiner = new StringJoiner(arraySeparator, arrayStart, arrayEnd);
			((Map<?, ?>) fieldValue).forEach((key, val) -> joiner.add(this.valToString(key) + fieldNameValueSeparator + this.valToString(val)));
			return joiner.toString();
		}

		if (Collection.class.isAssignableFrom(aClass)) {
			StringJoiner joiner = new StringJoiner(arraySeparator, arrayStart, arrayEnd);
			((Collection<?>) fieldValue).forEach(val -> joiner.add(this.valToString(val)));
			return joiner.toString();
		}

		return ToString.toString(fieldValue);
	}


	@Override
	public String toString() {
		if (Objects.isNull(obj)) {
			return nullText;
		}
		StringJoiner joiner = new StringJoiner(fieldSeparator, obj.getClass().getSimpleName() + contentStart, contentEnd);
		this.appendFields(joiner);
		return joiner.toString();
	}
}
