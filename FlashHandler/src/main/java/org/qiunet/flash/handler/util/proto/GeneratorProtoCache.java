package org.qiunet.flash.handler.util.proto;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/***
 *
 * @author qiunet
 * 2022/2/9 17:41
 */
public class GeneratorProtoCache {
	/**
	 * common proto class
	 */
	private final Set<Class<?>> commonProtoTypes = new HashSet<>();
	/**
	 * 类的proto 信息
	 */
	protected final Map<Class<?>, ClassProtoInfo> classInfo = Maps.newHashMap();
	/**
	 * 输出版本
	 */
	protected final ProtoIDLGenerator generator;

	public GeneratorProtoCache(ProtobufVersion version) {
		this.generator = new ProtoIDLGenerator(version);
	}

	public ProtobufVersion getVersion() {
		return generator.version;
	}

	public Set<Class<?>> getCommonProtoTypes() {
		return commonProtoTypes;
	}

	public ClassProtoInfo getClassProtoInfo(Class<?> clz) {
		return classInfo.computeIfAbsent(clz, ClassProtoInfo::new);
	}

	public void addCommonType(Class<?> clz) {
		if (clz.isEnum() && GeneratorProtoFeature.ENUM_TO_INT.prepare()) {
			return;
		}

		this.commonProtoTypes.add(clz);
	}
	/**
	 * 类的 proto info
	 */
	protected class ClassProtoInfo {
		/**
		 * 生成的 idl
		 */
		private final String idl;
		/**
		 * class
		 */
		private final Class<?> clz;
		/**
		 * 不包含 common.proto 里面的 class
		 */
		private Set<Class<?>> fieldInfos;


		private ClassProtoInfo(Class<?> clz) {
			this.idl = generator.getIDL(clz);
			this.clz = clz;
		}

		public Class<?> getClz() {
			return clz;
		}

		/**
		 * proto
		 * @return
		 */
		public String getIdl() {
			return idl;
		}

		/**
		 * 找到所有的子类以及子类字段的class集合.
		 * @param classInfoSet
		 */
		public void allChildFieldClazz(Set<ClassProtoInfo> classInfoSet) {
			classInfoSet.add(this);
			if (fieldInfos == null) {
				return;
			}

			fieldInfos.forEach(clz -> {
				getClassProtoInfo(clz).allChildFieldClazz(classInfoSet);
			});
		}

		/**
		 * 增加子类class
		 * @param info
		 */
		public void addFieldInfo(Class<?> info) {
			if (info.isAnnotationPresent(SkipProtoGenerator.class)) {
				return;
			}

			if (info.isEnum() && GeneratorProtoFeature.ENUM_TO_INT.prepare()) {
				return;
			}

			if (fieldInfos == null) {
				fieldInfos = Sets.newHashSet();
			}

			fieldInfos.add(info);
		}
	}
}
