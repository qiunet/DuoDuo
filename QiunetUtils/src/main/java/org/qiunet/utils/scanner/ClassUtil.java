package org.qiunet.utils.scanner;

import org.qiunet.utils.reflect.ReflectUtil;

import java.lang.reflect.Modifier;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Stream;

/***
 *
 * @author qiunet
 * 2023/5/5 18:09
 */
public final class ClassUtil {

	private static final ConcurrentHashMap<Class<?>, Object> beanInstances = new ConcurrentHashMap<>();

	private ClassUtil() {}

	/**
	 * 获取某个class的实例.
	 * 一般都是单例 如果不是. 注意可能跟业务保存的对象可能不一致.
	 * @param clazz class
	 * @param params 构造参数
	 * @return 实例
	 */
	public static Object getInstanceOfClass(Class<?> clazz, Object... params) {
		return beanInstances.computeIfAbsent(clazz, key -> {
			Optional<Object> first = Stream.of(key.getDeclaredFields())
				.filter(f -> Modifier.isStatic(f.getModifiers()))
				.filter(f -> f.getType() == key)
				.map(f -> ReflectUtil.getField(f, (Object) null))
				.filter(Objects::nonNull)
				.findFirst();

			if (first.isPresent()) {
				return first.get();
			}

			Object ret = ReflectUtil.newInstance(key, params);
			if (ret != null) {
				return ret;
			}

			throw new NullPointerException("can not get instance for class ["+key.getName()+"]");
		});
	}
}
