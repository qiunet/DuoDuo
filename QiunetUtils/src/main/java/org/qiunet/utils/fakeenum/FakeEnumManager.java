package org.qiunet.utils.fakeenum;

import com.google.common.collect.ComparisonChain;
import com.google.common.collect.Maps;
import net.bytebuddy.agent.ByteBuddyAgent;
import org.qiunet.utils.args.ArgsContainer;
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
	static <T extends BasicFakeEnum<T>> T valueOfEnum(String enumType, String name) {
		EnumData enumData = FakeEnumManager0.instance.enumConstants.get(enumType);
		if (enumData == null) {
			return null;
		}
		return (T) enumData.data.get(name);
	}

	private enum FakeEnumManager0 implements IApplicationContextAware {
		instance;

		FakeEnumManager0() {
			ByteBuddyAgent.install();
		}

		/**
		 * class → {name:instance}
		 */
		private final Map<String, EnumData> enumConstants = Maps.newHashMap();

		@Override
		public void setApplicationContext(IApplicationContext context, ArgsContainer argsContainer) throws Exception {
			Set<Class<?>> classSet = context.getTypesAnnotatedWith(EnumClass.class);
			classSet.forEach(this::enumClassProcess);
		}
		public void enumClassProcess(Class<?> clazz) {
			List<Field> fieldList = Stream.of(clazz.getDeclaredFields()).sorted(
					((o1, o2) -> ComparisonChain.start().compare(o1.getName(), o2.getName()).result())
			).collect(Collectors.toList());
			for (Field field : fieldList) {

				if (! BasicFakeEnum.class.isAssignableFrom(field.getType())) {
					throw new CustomException("FakeEnum type [{}] need implement IFakeEnum interface.", field.getType().getName());
				}

				if (! Modifier.isStatic(field.getModifiers())) {
					throw new CustomException("FakeEnum [{}#{}] need be static", field.getDeclaringClass().getName(), field.getName());
				}

				EnumData data = enumConstants.computeIfAbsent(field.getType().getName(), key -> new EnumData());
				String enumName = field.getName();
				if (data.containsKey(enumName)) {
					throw new CustomException("FakeEnum name [{}] in field [{}] is repeated", enumName, field.getName());
				}

				BasicFakeEnum fieldVal = (BasicFakeEnum) ReflectUtil.getField(field, (Object) null);
				if (fieldVal == null) {
					fieldVal = (BasicFakeEnum) ReflectUtil.newInstance(field.getType());
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

	private static class EnumData {
		/**
		 * name → 枚举
		 */
		private final Map<String, BasicFakeEnum> data = Maps.newHashMap();
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
		 * 转成values 数组
		 * @return
		 */
		public BasicFakeEnum[] toValues() {
			return data.values().stream().sorted((o1, o2) -> ComparisonChain.start().compare(o1.ordinal(), o2.ordinal()).result()).toArray(BasicFakeEnum[]::new);
		}

		/**
		 * 增加这个枚举
		 * @param name
		 * @param fakeEnum
		 */
		public void add(String name, BasicFakeEnum fakeEnum) {
			this.data.put(name, fakeEnum);
		}
	}

}
