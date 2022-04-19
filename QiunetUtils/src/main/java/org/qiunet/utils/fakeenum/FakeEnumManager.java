package org.qiunet.utils.fakeenum;

import com.google.common.collect.ComparisonChain;
import com.google.common.collect.Maps;
import org.qiunet.utils.args.ArgsContainer;
import org.qiunet.utils.async.LazyLoader;
import org.qiunet.utils.common.collector.DCollectors;
import org.qiunet.utils.exceptions.CustomException;
import org.qiunet.utils.reflect.ReflectUtil;
import org.qiunet.utils.scanner.IApplicationContext;
import org.qiunet.utils.scanner.IApplicationContextAware;
import org.qiunet.utils.scanner.ScannerType;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/***
 * 假枚举管理
 * @author qiunet
 * 2022/1/3 08:49
 */
class FakeEnumManager {
	/**
	 * 获得指定类型以及名字的的枚举
	 * @param enumType
	 * @param name
	 * @param <T>
	 * @return
	 */
	static <T extends FakeEnum<T>> T valueOfEnum(String enumType, String name) {
		EnumData<T> enumData = (EnumData<T>) FakeEnumManager0.instance.enumConstants.get(enumType);
		if (enumData == null) {
			return null;
		}
		return enumData.data.get(name);
	}

	/**
	 * Return all enum constants by type
	 * @param enumType
	 * @param <T>
	 * @return
	 */
	static <T extends FakeEnum<T>> List<T> values(String enumType) {
		EnumData<T> enumData = (EnumData<T>) FakeEnumManager0.instance.enumConstants.get(enumType);
		if (enumData == null) {
			return null;
		}
		return enumData.values();
	}

	private enum FakeEnumManager0 implements IApplicationContextAware {
		instance;

		FakeEnumManager0() {

		}

		/**
		 * class → {name:instance}
		 */
		private final Map<String, EnumData<? extends FakeEnum>> enumConstants = Maps.newHashMap();

		@Override
		public void setApplicationContext(IApplicationContext context, ArgsContainer argsContainer) throws Exception {
			Set<Class<?>> classSet = context.getTypesAnnotatedWith(FakeEnumClass.class);
			classSet.forEach(this::enumClassProcess);
		}
		public void enumClassProcess(Class<?> clazz) {
			List<Field> fieldList = Stream.of(clazz.getDeclaredFields()).sorted(
					((o1, o2) -> ComparisonChain.start().compare(o1.getName(), o2.getName()).result())
			).collect(Collectors.toList());
			for (Field field : fieldList) {
				if (! FakeEnum.class.isAssignableFrom(field.getType())) {
					continue;
				}

				if (! Modifier.isStatic(field.getModifiers())) {
					throw new CustomException("FakeEnum [{}#{}] need be static", field.getDeclaringClass().getName(), field.getName());
				}

				EnumData data = enumConstants.computeIfAbsent(field.getType().getName(), key -> new EnumData());
				String enumName = field.getName();
				if (data.containsKey(enumName)) {
					throw new CustomException("FakeEnum name [{}] in field [{}] is repeated", enumName, field.getName());
				}

				FakeEnum fieldVal = (FakeEnum) ReflectUtil.getField(field, (Object) null);
				if (fieldVal != null && !Modifier.isFinal(field.getModifiers())) {
					throw new CustomException("Valued field [{}] need be a final field", field.getName());
				}

				if (fieldVal == null) {
					fieldVal = (FakeEnum) ReflectUtil.newInstance(field.getType());
					ReflectUtil.setField(null, field, fieldVal);
				}
				ReflectUtil.setField(fieldVal, "ordinal", data.id.getAndIncrement());
				ReflectUtil.setField(fieldVal, "name", enumName);
				data.add(enumName, fieldVal);
			}
		}

		@Override
		public ScannerType scannerType() {
			return ScannerType.FAKE_ENUM;
		}
	}

	private static class EnumData<E extends FakeEnum<E>> {
		private final LazyLoader<List<E>> values = new LazyLoader<>(this::toValues);
		/**
		 * name → 枚举
		 */
		private final Map<String, E> data = Maps.newHashMap();
		/**
		 *
		 */
		private final AtomicInteger id = new AtomicInteger();

		public EnumData() {}

		/**
		 * 是否包含 name 数据
		 * @param name
		 * @return
		 */
		public boolean containsKey(String name) {
			return data.containsKey(name);
		}
		/**
		 * 得到values
		 * @return
		 */
		public List<E> values() {
			return values.get();
		}
		/**
		 * 转成values 数组
		 * @return
		 */
		private List<E> toValues() {
			return data.values().stream()
					.sorted((o1, o2) -> ComparisonChain.start().compare(o1.ordinal(), o2.ordinal()).result())
					.collect(DCollectors.toSafeList());
		}

		/**
		 * 增加这个枚举
		 * @param name
		 * @param fakeEnum
		 */
		public void add(String name, E fakeEnum) {
			this.data.put(name, fakeEnum);
		}
	}

}
