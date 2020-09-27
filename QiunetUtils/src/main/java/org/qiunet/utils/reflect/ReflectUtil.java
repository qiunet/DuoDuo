package org.qiunet.utils.reflect;

import org.qiunet.utils.logger.LoggerType;
import org.slf4j.Logger;

import java.lang.reflect.Field;

/***
 * 反射工具类
 *
 * @author qiunet
 * 2020-09-27 11:00
 */
public final class ReflectUtil {
	private static final Logger LOGGER = LoggerType.DUODUO.getLogger();
	private ReflectUtil(){}

	/**
	 * Attempt to find a {@link Field field} on the supplied {@link Class} with
	 * the supplied <code>name</code>. Searches all superclasses up to
	 * {@link Object}.
	 *
	 * @param clazz
	 *            the class to introspect
	 * @param name
	 *            the name of the field
	 * @return the corresponding Field object, or <code>null</code> if not found
	 */
	public static Field findField(Class clazz, String name) {
		return findField(clazz, name, null);
	}

	/**
	 * Attempt to find a {@link Field field} on the supplied {@link Class} with
	 * the supplied <code>name</code> and/or {@link Class type}. Searches all
	 * superclasses up to {@link Object}.
	 *
	 * @param clazz
	 *            the class to introspect
	 * @param name
	 *            the name of the field (may be <code>null</code> if type is
	 *            specified)
	 * @param type
	 *            the type of the field (may be <code>null</code> if name is
	 *            specified)
	 * @return the corresponding Field object, or <code>null</code> if not found
	 */
	public static Field findField(Class clazz, String name, Class type) {
		if (clazz == null) {
			throw new IllegalArgumentException("Class must not be null");
		}
		if (name == null && type == null) {
			throw new IllegalArgumentException(
				"Either name or type of the field must be specified");
		}
		Class searchType = clazz;
		while (!Object.class.equals(searchType) && searchType != null) {
			Field[] fields = searchType.getDeclaredFields();
			for (int i = 0; i < fields.length; i++) {
				Field field = fields[i];
				if ((name == null || name.equals(field.getName()))
					&& (type == null || type.equals(field.getType()))) {
					return field;
				}
			}
			searchType = searchType.getSuperclass();
		}
		return null;
	}

	/**
	 * Get the field represented by the supplied {@link Field field object} on
	 * the specified {@link Object target object}. In accordance with
	 * {@link Field#get(Object)} semantics, the returned value is automatically
	 * wrapped if the underlying field has a primitive type.
	 * <p>
	 * Thrown exceptions are handled via a call to
	 *
	 * @param t
	 *            the target object from which to get the field
	 * @param name
	 *            the field to get
	 * @return the field's current value
	 */
	public static Object getField(Object t, String name) {
		Field field = findField(t.getClass(), name);
		if (field == null) {
			return null;
		}
		return getField(field, t);
	}

	/**
	 * Set the field represented by the supplied {@link Field field object} on
	 * the specified {@link Object target object} to the specified
	 * <code>value</code>. In accordance with {@link Field#set(Object, Object)}
	 * semantics, the new value is automatically unwrapped if the underlying
	 * field has a primitive type.
	 * <p>
	 * Thrown exceptions are handled via a call to
	 *
	 * @param t
	 *            the target object on which to set the field
	 * @param name
	 *            the field to set
	 * @param value
	 *            the value to set; may be <code>null</code>
	 */
	public static void setField(Object t, String name, Object value) {
		Field field = findField(t.getClass(), name);
		if (field == null) {
			return;
		}
		field.setAccessible(true);
		try {
			field.set(t, value);
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
		}
	}

	/**
	 * Get the field represented by the supplied {@link Field field object} on
	 * the specified {@link Object target object} . In accordance with {@link Field#get(Object)}
	 * <p>
	 * Thrown exceptions are handled via a call to
	 *
	 * @param field
	 *            the field to get
     * @param obj
	 * 	         the field declare object instance. if field is static. it is null.
	 */
	public static Object getField(Field field, Object obj) {
		if (field == null) {
			return null;
		}
		field.setAccessible(true);
		try {
			return field.get(obj);
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
		}
		return null;
	}

}
