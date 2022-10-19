package org.qiunet.utils.reflect;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.ClassUtils;
import org.qiunet.utils.exceptions.CustomException;
import org.qiunet.utils.logger.LoggerType;
import org.slf4j.Logger;

import java.lang.annotation.Annotation;
import java.lang.reflect.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.BiPredicate;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.IntStream;

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
	 * Attempt to find a {@link Field field} on the supplied {@link Class} with the
	 * supplied {@code name}. Searches all superclasses up to {@link Object}.
	 * @param clazz the class to introspect
	 * @return the corresponding Field object, or {@code null} if not found
	 */
	public static List<Field> findFieldList(Class<?> clazz, Predicate<Field> filter) {
		Class<?> searchType = clazz;
		List<Field> list = Lists.newArrayListWithCapacity(5);
		while (Object.class != searchType && searchType != null) {
			Field[] fields = getDeclaredFields(searchType);
			for (Field field : fields) {
				if (filter.test(field)) {
					list.add(field);
				}
			}
			searchType = searchType.getSuperclass();
		}
		return list;
	}
	/**
	 * Attempt to find a {@link Field field} on the supplied {@link Class} with the
	 * supplied {@code name}. Searches all superclasses up to {@link Object}.
	 * @param clazz the class to introspect
	 * @param name the name of the field
	 * @return the corresponding Field object, or {@code null} if not found
	 */
	public static Field findField(Class<?> clazz, String name) {
		return findField(clazz, name, null);
	}

	/**
	 * Attempt to find a {@link Field field} on the supplied {@link Class} with the
	 * supplied {@code name} and/or {@link Class type}. Searches all superclasses
	 * up to {@link Object}.
	 * @param clazz the class to introspect
	 * @param name the name of the field (may be {@code null} if type is specified)
	 * @param type the type of the field (may be {@code null} if name is specified)
	 * @return the corresponding Field object, or {@code null} if not found
	 */
	public static Field findField(Class<?> clazz, String name, Class<?> type) {
		Preconditions.checkNotNull(clazz, "Class must not be null");
		Preconditions.checkState(name != null || type != null, "Either name or type of the field must be specified");
		Class<?> searchType = clazz;
		while (Object.class != searchType && searchType != null) {
			Field[] fields = getDeclaredFields(searchType);
			for (Field field : fields) {
				if ((name == null || name.equals(field.getName())) &&
					(type == null || type.equals(field.getType()))) {
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
	 * Make the given method accessible, explicitly setting it accessible if
	 * necessary. The {@code setAccessible(true)} method is only called
	 * when actually necessary, to avoid unnecessary conflicts with a JVM
	 * SecurityManager (if active).
	 * @param method the method to make accessible
	 * @see java.lang.reflect.Method#setAccessible
	 */
	@SuppressWarnings("deprecation")  // on JDK 9
	public static Method makeAccessible(Method method) {
		if ((!Modifier.isPublic(method.getModifiers()) ||
			!Modifier.isPublic(method.getDeclaringClass().getModifiers())) && !method.isAccessible()) {
			method.setAccessible(true);
		}
		return method;
	}
	/**
	 * Make the given field accessible, explicitly setting it accessible if
	 * necessary. The {@code setAccessible(true)} method is only called
	 * when actually necessary, to avoid unnecessary conflicts with a JVM
	 * SecurityManager (if active).
	 * @param field the field to make accessible
	 * @see java.lang.reflect.Field#setAccessible
	 */
	@SuppressWarnings("deprecation")  // on JDK 9
	public static Field makeAccessible(Field field) {
		if ((!Modifier.isPublic(field.getModifiers()) ||
			!Modifier.isPublic(field.getDeclaringClass().getModifiers()) ||
			Modifier.isFinal(field.getModifiers())) && !field.isAccessible()) {
			field.setAccessible(true);
		}
		return field;
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
	 * @param declaringObj
	 *            the target object on which to set the field
	 * @param name
	 *            the field to set
	 * @param value
	 *            the value to set; may be <code>null</code>
	 */
	public static void setField(Object declaringObj, String name, Object value) {
		Field field = findField(declaringObj.getClass(), name);
		if (field == null) {
			return;
		}
		setField(declaringObj, field, value);
	}

	/**
	 * 给字段设置值
	 * @param declaringObj the target object on which to set the field
	 * @param field the field to set
	 * @param value the value to set; may be <code>null</code>
	 */
	public static void setField(Object declaringObj, Field field, Object value) {
		makeAccessible(field);
		try {
			field.set(declaringObj, value);
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
		makeAccessible(field);
		try {
			return field.get(obj);
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
		}
		return null;
	}

	/**
	 * 生成新的对象
	 * @param clazz
	 * @param params
	 * @param <T>
	 * @return
	 */
	public static <T> T newInstance(Class<T> clazz, Object... params) {
		Constructor<T> constructor = getMatchConstructor(clazz, params);
		return newInstance(constructor, params);
	}
	/**
	 * 获得匹配的构造
	 * @param clazz
	 * @param params
	 * @param <T>
	 * @return
	 */
	public static <T> Constructor<T> getMatchConstructor(Class<T> clazz, Object... params) {
		Class<?> [] classes = ClassUtils.toClass(params);
		Constructor<T>[] constructors = (Constructor<T>[]) clazz.getDeclaredConstructors();
		for (Constructor<T> constructor : constructors) {
			if (constructor.getParameterCount() != classes.length) {
				continue;
			}

			boolean allMatch = IntStream.range(0, classes.length).mapToObj(i -> classes[i] == constructor.getParameterTypes()[i]).allMatch(Boolean::booleanValue);
			if (! allMatch) {
				continue;
			}

			constructor.setAccessible(true);
			return constructor;
		}
		return null;
	}

	/**
	 * 生成新的对象.
	 * @param constructor
	 * @param params
	 * @param <T>
	 * @return
	 */
	public static <T> T newInstance(Constructor<T> constructor, Object... params) {
		if (constructor == null) {
			return null;
		}

		constructor.setAccessible(true);
		try {
			return constructor.newInstance(params);
		} catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
			LOGGER.error(e.getMessage(), e);
		}
		return null;
	}

	/**
	 * Invoke the given callback on all fields in the target class, going up the
	 * class hierarchy to get all declared fields.
	 * @param clazz the target class to analyze
	 * @param consumer the callback to invoke for each field
	 * @throws IllegalStateException if introspection fails
	 */
	public static void doWithFields(Class<?> clazz, Consumer<Field> consumer) {
		doWithFields(clazz, consumer, null);
	}

	/**
	 * Invoke the given callback on all fields in the target class, going up the
	 * class hierarchy to get all declared fields.
	 * @param clazz the target class to analyze
	 * @param consumer the callback to invoke for each field
	 * @param predicate the filter that determines the fields to apply the callback to
	 * @throws IllegalStateException if introspection fails
	 */
	public static void doWithFields(Class<?> clazz, Consumer<Field> consumer, Predicate<Field> predicate) {
		// Keep backing up the inheritance hierarchy.
		Class<?> targetClass = clazz;
		do {
			Field[] fields = getDeclaredFields(targetClass);
			for (Field field : fields) {
				if (predicate != null && !predicate.test(field)) {
					continue;
				}
				try {
					makeAccessible(field);
					consumer.accept(field);
				}
				catch (Exception ex) {
					throw new CustomException(ex, "Not allowed to access field '{}'! " , field.getName());
				}
			}
			targetClass = targetClass.getSuperclass();
		}
		while (targetClass != null && targetClass != Object.class);
	}

	private static final Field[] EMPTY_FIELD_ARRAY = new Field[0];
	/**
	 * Cache for {@link Class#getDeclaredFields()}, allowing for fast iteration.
	 */
	private static final Map<Class<?>, Field[]> declaredFieldsCache = new ConcurrentHashMap<>(256);
	/**
	 * This variant retrieves {@link Class#getDeclaredFields()} from a local cache
	 * in order to avoid the JVM's SecurityManager check and defensive array copying.
	 * @param clazz the class to introspect
	 * @return the cached array of fields
	 * @throws IllegalStateException if introspection fails
	 * @see Class#getDeclaredFields()
	 */
	public static Field[] getDeclaredFields(Class<?> clazz) {
		Preconditions.checkNotNull(clazz, "Class must not be null");
		return declaredFieldsCache.computeIfAbsent(clazz, key -> {
			Field[] declaredFields = key.getDeclaredFields();
			return (declaredFields.length == 0 ? EMPTY_FIELD_ARRAY : declaredFields);
		});
	}

	/**
	 * Perform the given callback operation on all matching methods of the given
	 * class and superclasses.
	 * <p>The same named method occurring on subclass and superclass will appear
	 * twice, unless excluded by a {@link Predicate}.
	 * @param clazz the class to introspect
	 * @param mc the callback to invoke for each method
	 * @throws IllegalStateException if introspection fails
	 * @see #doWithMethods(Class, Consumer, Predicate)
	 */
	public static void doWithMethods(Class<?> clazz, Consumer<Method> mc) {
		doWithMethods(clazz, mc, null);
	}

	/**
	 * Perform the given callback operation on all matching methods of the given
	 * class and superclasses (or given interface and super-interfaces).
	 * <p>The same named method occurring on subclass and superclass will appear
	 * twice, unless excluded by the specified {@link Predicate}.
	 * @param clazz the class to introspect
	 * @param mc the callback to invoke for each method
	 * @param mf the filter that determines the methods to apply the callback to
	 * @throws IllegalStateException if introspection fails
	 */
	public static void doWithMethods(Class<?> clazz, Consumer<Method> mc, Predicate<Method> mf) {
		// Keep backing up the inheritance hierarchy.
		Method[] methods = getDeclaredMethods(clazz, false);
		for (Method method : methods) {
			if (mf != null && !mf.test(method)) {
				continue;
			}
			try {
				makeAccessible(method);
				mc.accept(method);
			}
			catch (Exception ex) {
				throw new CustomException(ex, "Not allowed to access method [{}]", method.getName());
			}
		}
		if (clazz.getSuperclass() != null && (mf != USER_DECLARED_METHODS || clazz.getSuperclass() != Object.class)) {
			doWithMethods(clazz.getSuperclass(), mc, mf);
		}
		else if (clazz.isInterface()) {
			for (Class<?> superIfc : clazz.getInterfaces()) {
				doWithMethods(superIfc, mc, mf);
			}
		}
	}

	/**
	 * Get all declared methods on the leaf class and all superclasses.
	 * Leaf class methods are included first.
	 * @param leafClass the class to introspect
	 * @throws IllegalStateException if introspection fails
	 */
	public static Method[] getAllDeclaredMethods(Class<?> leafClass) {
		final List<Method> methods = new ArrayList<>(32);
		doWithMethods(leafClass, methods::add);
		return methods.toArray(EMPTY_METHOD_ARRAY);
	}
	private static final Method[] EMPTY_METHOD_ARRAY = new Method[0];
	/**
	 * Pre-built MethodFilter that matches all non-bridge non-synthetic methods
	 * which are not declared on {@code java.lang.Object}.
	 * @since 3.0.5
	 */
	public static final Predicate<Method> USER_DECLARED_METHODS = (method -> !method.isBridge() && !method.isSynthetic());
	/**
	 * Cache for {@link Class#getDeclaredMethods()} plus equivalent default methods
	 * from Java 8 based interfaces, allowing for fast iteration.
	 */
	private static final Map<Class<?>, Method[]> declaredMethodsCache = new ConcurrentHashMap<>(256);

	/**
	 * Variant of {@link Class#getDeclaredMethods()} that uses a local cache in
	 * order to avoid the JVM's SecurityManager check and new Method instances.
	 * In addition, it also includes Java 8 default methods from locally
	 * implemented interfaces, since those are effectively to be treated just
	 * like declared methods.
	 * @param clazz the class to introspect
	 * @return the cached array of methods
	 * @throws IllegalStateException if introspection fails
	 * @since 5.2
	 * @see Class#getDeclaredMethods()
	 */
	public static Method[] getDeclaredMethods(Class<?> clazz) {
		return getDeclaredMethods(clazz, true);
	}

	private static Method[] getDeclaredMethods(Class<?> clazz, boolean defensive) {
		Preconditions.checkNotNull(clazz, "Class must not be null");
		Method[] result = declaredMethodsCache.get(clazz);
		if (result == null) {
			try {
				Method[] declaredMethods = clazz.getDeclaredMethods();
				List<Method> defaultMethods = findConcreteMethodsOnInterfaces(clazz);
				if (defaultMethods != null) {
					result = new Method[declaredMethods.length + defaultMethods.size()];
					System.arraycopy(declaredMethods, 0, result, 0, declaredMethods.length);
					int index = declaredMethods.length;
					for (Method defaultMethod : defaultMethods) {
						result[index] = defaultMethod;
						index++;
					}
				}
				else {
					result = declaredMethods;
				}
				declaredMethodsCache.put(clazz, (result.length == 0 ? EMPTY_METHOD_ARRAY : result));
			}
			catch (Throwable ex) {
				throw new IllegalStateException("Failed to introspect Class [" + clazz.getName() +
					"] from ClassLoader [" + clazz.getClassLoader() + "]", ex);
			}
		}
		return (result.length == 0 || !defensive) ? result : result.clone();
	}


	private static List<Method> findConcreteMethodsOnInterfaces(Class<?> clazz) {
		List<Method> result = null;
		for (Class<?> ifc : clazz.getInterfaces()) {
			for (Method ifcMethod : ifc.getMethods()) {
				if (!Modifier.isAbstract(ifcMethod.getModifiers())) {
					if (result == null) {
						result = new ArrayList<>();
					}
					result.add(ifcMethod);
				}
			}
		}
		return result;
	}

	/**
	 * 找出Class 上对应的泛型参数类型
	 * @param oriClazz 原始class
	 * @param filter 类型过滤 仅给出泛型class
	 * @return
	 */
	public static Class<?> findGenericParameterizedType(Class<?> oriClazz, Predicate<Class<?>> filter) {
		return findGenericParameterizedType(oriClazz, (c1, c2) -> filter == null || filter.test(c2));
	}
	/**
	 * 找出Class 上对应的泛型参数类型
	 * @param oriClazz 原始class
	 * @param filter 类型过滤 给出所在class 以及 泛型class
	 * @return
	 */
	public static Class<?> findGenericParameterizedType(Class<?> oriClazz, BiPredicate<Class<?>, Class<?>> filter) {
		Class<?> clazz = oriClazz;
		do {
			if (! (clazz.getGenericSuperclass() instanceof ParameterizedType)) {
				clazz = clazz.getSuperclass();
				continue;
			}
			Type[] types = ((ParameterizedType) clazz.getGenericSuperclass()).getActualTypeArguments();
			for (Type type : types) {
				if (type instanceof Class && filter.test(clazz, (Class<?>) type)) {
					return  (Class<?>) type;
				}
			}
			clazz = clazz.getSuperclass();
		}while (clazz != Object.class);
		return null;
	}

	/**
	 * 得到list field class的泛型
	 * @return
	 */
	public static Class<?> getListGenericParameterizedType(Field field) {
		if (! List.class.isAssignableFrom(field.getType())) {
			throw new CustomException("Field {}.{} not list type field!", field.getDeclaringClass().getName(), field.getName());
		}

		Type genericType = field.getGenericType();
		if (! (genericType instanceof ParameterizedType)) {
			throw new CustomException("Field {}.{} not have generic type field!", field.getDeclaringClass().getName(), field.getName());
		}

		ParameterizedType ptype = (ParameterizedType) genericType;
		Type[] actualTypeArguments = ptype.getActualTypeArguments();
		return (Class<?>) actualTypeArguments[0];
	}

	/**
	 * 修改注解的值
	 * @param annotation 注解对象
	 * @param key 注解字段名.
	 * @param value 设置的值
	 */
	public static void modifyAnnotationValue(Annotation annotation, String key, Object value) {
		InvocationHandler invocationHandler = Proxy.getInvocationHandler(annotation);
		try {
			Field memberValues = invocationHandler.getClass().getDeclaredField("memberValues");
			makeAccessible(memberValues);

			Map<String, Object> obj = (Map<String, Object>) memberValues.get(invocationHandler);
			obj.put(key, value);
		} catch (Exception e) {
			LoggerType.DUODUO.error("modifyAnnotationValue error", e);
		}
	}
}
