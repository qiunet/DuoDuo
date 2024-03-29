package org.qiunet.flash.handler.util.proto;

import com.baidu.bjf.remoting.protobuf.EnumReadable;
import com.baidu.bjf.remoting.protobuf.FieldType;
import com.baidu.bjf.remoting.protobuf.annotation.Ignore;
import com.baidu.bjf.remoting.protobuf.annotation.Protobuf;
import com.baidu.bjf.remoting.protobuf.annotation.ProtobufClass;
import com.baidu.bjf.remoting.protobuf.utils.FieldInfo;
import com.baidu.bjf.remoting.protobuf.utils.ProtobufProxyUtils;
import com.google.common.collect.ComparisonChain;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.qiunet.flash.handler.common.id.IProtocolId;
import org.qiunet.flash.handler.context.request.data.ChannelData;
import org.qiunet.flash.handler.context.request.data.ChannelDataMapping;
import org.qiunet.flash.handler.context.request.data.IChannelData;
import org.qiunet.utils.exceptions.CustomException;
import org.qiunet.utils.file.FileUtil;
import org.qiunet.utils.reflect.ReflectUtil;
import org.qiunet.utils.string.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.lang.reflect.Field;
import java.util.*;
import java.util.stream.Stream;

/***
 *
 * @author qiunet
 * 2022/2/9 09:45
 */
public final class ProtoIDLGenerator {
	static {
		ProtobufProxyUtils.FIELD_FILTER_STARTS.add("$");
		ProtobufProxyUtils.FIELD_FILTER_STARTS.add("_");
	}

	/** Logger for this class. */
	private static final Logger LOGGER = LoggerFactory.getLogger(ProtoIDLGenerator.class.getName());

	final ProtobufVersion version;

	public ProtoIDLGenerator(ProtobufVersion version) {
		if (version == ProtobufVersion.V3) {
			// V3 支持enum了. V2 部分语言不支持
			GeneratorProtoFeature.features.remove(GeneratorProtoFeature.ENUM_TO_INT);
		}
		this.version = version;
	}

	/**
	 * get IDL content from class.
	 *
	 * @param cls target class to parse for IDL message.
	 * @return protobuf IDL content in string
	 * @see Protobuf
	 */
	public String getIDL(final Class<?> cls) {
		Ignore ignore = cls.getAnnotation(Ignore.class);
		if (ignore != null) {
			LOGGER.info("class '{}' marked as @Ignore annotation, create IDL ignored.", cls.getName());
			return null;
		}


		StringBuilder code = new StringBuilder();
		this.generateIDL(code, cls);
		return code.toString();
	}

	/***
	 * enum 的注解
	 */
	private final Map<Class<?>, String> enumComment = Maps.newHashMap();
	private String getEnumComment(Class<?> enumClass) {
		if (enumClass == null || ! enumClass.isEnum()) {
			return StringUtil.EMPTY_STRING;
		}
		return enumComment.computeIfAbsent(enumClass, clz -> {
			StringBuilder sb = new StringBuilder();
			Stream.of(clz.getFields()).filter(f -> f.getType() == clz).forEach(field -> {
				try {
					String name = field.getName();
					Enum value = Enum.valueOf((Class<? extends Enum>) clz, name);
					if (value instanceof EnumReadable) {
						sb.append(((EnumReadable) value).value());
					} else {
						sb.append(value.ordinal());
					}
					sb.append("=");
					Protobuf annotation = field.getAnnotation(Protobuf.class);
					if (annotation != null && !StringUtil.isEmpty(annotation.description())) {
						sb.append(annotation.description()).append("\t");
					}
				} catch (Exception ignored) {}
			});
			return sb.toString();
		});
	}

	/**
	 * 生成普通obj的
	 * @param code
	 * @param cls
	 */
	private void generateIDL(StringBuilder code, Class<?> cls) {
		if (cls.isEnum()) {
			this.generateEnumIDL(code, (Class<Enum>) cls);
			return;
		}

		code.append(generatorComment(cls)).append("message ").append(cls.getSimpleName()).append(" {  \n");

		List<FieldInfo> fieldInfos = ProtobufProxyUtils.fetchFieldInfos(cls, false);
		for (FieldInfo field : fieldInfos) {
			if (field.hasDescription()) {
				code.append("\t// ").append(field.getDescription()).append("\n");
			}

			String required = version.getFieldDescribe();
			Class<?> c = field.getField().getType();
			if (field.isList()) {
				c = ReflectUtil.getListGenericParameterizedType(field.getField());
				required = "\trepeated ";
			}

			FieldType fieldType = field.getFieldType();
			if (field.getField().isAnnotationPresent(ProtoSubstitute.class)) {
				c = field.getField().getAnnotation(ProtoSubstitute.class).value();
				fieldType = ProtobufProxyUtils.TYPE_MAPPING.get(c);
				if (fieldType == null) {
					// check if type is enum
					if (Enum.class.isAssignableFrom(c)) {
						fieldType = FieldType.ENUM;
					} else if (field.isMap()) {
						fieldType = FieldType.MAP;
					} else {
						fieldType = FieldType.OBJECT;
					}
				}
			}

			String fieldTypeName = fieldType.getType().toLowerCase();
			if (fieldType == FieldType.OBJECT || fieldType == FieldType.ENUM) {
				fieldTypeName = c.getSimpleName();
			}

			if (ProtobufProxyUtils.isScalarType(c)) {
				fieldTypeName = fieldType.getType().toLowerCase();
			}

			if (c.isEnum() && GeneratorProtoFeature.ENUM_TO_INT.prepare()) {
				code.append("\t// ").append(this.getEnumComment(c)).append("\n");
				fieldTypeName = FieldType.INT32.getType();
			}

			if (fieldType == FieldType.MAP) {
				Class keyClass = field.getGenericKeyType();
				Class valueClass = field.getGenericeValueType();
				fieldTypeName = fieldTypeName + "<" + ProtobufProxyUtils.processProtobufType(keyClass) + ", ";
				fieldTypeName = fieldTypeName + ProtobufProxyUtils.processProtobufType(valueClass)  + ">";
				required = "\t";
			}

			code.append(required).append(fieldTypeName).append(" ").append(field.getField().getName()).append("=").append(field.getOrder());
			if (field.isList())code.append(version.getRepeatedFieldDescribe(field));
			code.append(";\n");

		}
		code.append("}\n\n\n");
	}
	/**
	 * 生成枚举的
	 * @param code
	 * @param cls
	 */
	private void generateEnumIDL(StringBuilder code, Class<Enum> cls) {
		if (GeneratorProtoFeature.ENUM_TO_INT.prepare()) {
			return;
		}

		code.append(this.generatorComment(cls));
		code.append("enum ").append(cls.getSimpleName()).append(" {  \n");

		Field[] fields = cls.getFields();
		for (Field field : fields) {
			if (field.getType() != cls) {
				continue;
			}

			try {
				String name = field.getName();
				Enum value = Enum.valueOf(cls, name);

				Protobuf annotation = field.getAnnotation(Protobuf.class);
				if (annotation != null && !StringUtil.isEmpty(annotation.description())) {
					code.append("//").append(annotation.description()).append("\n");
				}
				code.append('\t').append(cls.getSimpleName()).append("_").append(name).append("=");

				if (value instanceof EnumReadable) {
					code.append(((EnumReadable) value).value());
				} else {
					code.append(value.ordinal());
				}
				code.append(";\n");
			} catch (Exception ignored) {
			}
		}

		code.append("}\n");
		code.append("\n\n");
	}
	/**
	 * 获得某个pbData的protocolID
	 * @param clz
	 * @return
	 */
	public static int getProtocolId(Class<?> clz) {
		if (IChannelData.class.isAssignableFrom(clz)) {
			return ChannelDataMapping.protocolId((Class<? extends IChannelData>) clz);
		}
		return 0;
	}


	/**
	 * 生成 协议名 协议ID  映射关系
	 * @param enumName
	 * @param comment
	 * @param code
	 * @param protoContent
	 */
	private void generateProtocolIdMapping(String enumName, String comment, StringBuilder code, Map<String, Integer> protoContent) {
		code.append("// ").append(comment).append("\n");
		code.append("enum ").append(enumName).append(" {  \n");
		Lists.newArrayList(protoContent.entrySet()).stream().sorted((o1, o2) -> ComparisonChain.start().compare(o1.getValue(), o2.getValue()).result())
				.forEach(en -> code.append(enumName).append("_").append(en.getKey()).append("=").append(en.getValue()).append(";\n"));
		code.append("}\n\n");
	}
	private final Map<Class<?>, Set<String>> outputNames = Maps.newHashMapWithExpectedSize(256);
	/**
	 * 生成指定class的IDL
	 * @param cache
	 * @param code
	 * @param classes
	 */
	public boolean generatorIDLs(GeneratorProtoCache cache, String outputName, StringBuilder code, boolean commonProto, Class<?>... classes) {
		return this.generatorIDLs(cache, outputName, code, commonProto, Arrays.asList(classes));
	}
	/**
	 * 生成指定class的IDL
	 * @param cache
	 * @param code
	 * @param classes
	 */
	public boolean generatorIDLs(GeneratorProtoCache cache, String outputName, StringBuilder code, boolean commonProto, Collection<Class<?>> classes) {
		Set<GeneratorProtoCache.ClassProtoInfo> set = new HashSet<>();
		classes.forEach(clz -> cache.getClassProtoInfo(clz).allChildFieldClazz(set));
		set.stream()
		.filter(info -> commonProto || !cache.getCommonProtoTypes().contains(info.getClz()))
		.sorted(((o1, o2) -> ComparisonChain.start().compare(o1.getClz().getSimpleName(), o2.getClz().getSimpleName()).result()))
		.forEach(info -> {
			outputNames.computeIfAbsent(info.getClz(), key -> new HashSet<>(4)).add(outputName);
			code.append(info.getIdl()).append("\n");
		});
		return set.stream().anyMatch(info -> cache.getCommonProtoTypes().contains(info.getClz()));
	}

	/**
	 * 分析依赖
	 */
	public void analyseDepends() {
		StringBuilder sb = new StringBuilder();
		outputNames.forEach((clz, set) -> {
			if (set.size() > 1) {
				sb.append(StringUtil.slf4jFormat("\nClass [{}] proto data define repeated in [{}]!", clz.getName(), Arrays.toString(set.toArray())));
			}
		});
		if (sb.length() > 0) {
			throw new CustomException(sb.append("\n").toString());
		}
	}
	/**
	 *
	 * @param clz
	 * @return
	 */
	private String generatorComment(Class<?> clz) {
		ProtobufClass annotation = clz.getAnnotation(ProtobufClass.class);
		if (annotation == null) {
			return "";
		}
		String comment = annotation.description();
		StringBuilder sb = new StringBuilder();
		sb.append("// ");
		if (IChannelData.class.isAssignableFrom(clz)) {
			int protocolId = getProtocolId(clz);
			sb.append("[ProtocolId = ").append(protocolId).append(" ] ");

			ChannelData channelData = clz.getAnnotation(ChannelData.class);
			comment = channelData.desc();
			if (channelData.kcp()) {
				sb.append(" KCP协议 ");
			}
		}

		sb.append(comment).append("\n");
		return sb.toString();
	}

	/**
	 * 获取 IChannelData 里面的所有其它非自有类型
	 * @param cls
	 * @return 是否包含 common message
	 */
	public static boolean recursiveObjClass( Class<?> cls, GeneratorProtoCache cache) {
		return recursiveObjClass(cls, cache, false, new HashSet<>());
	}

	private static boolean recursiveObjClass( Class<?> cls, GeneratorProtoCache cache, boolean parentCommon, Set<Class<?>> handleCls) {
		boolean haveCommonProtoMessage = false, currCommonProtoMessage = parentCommon;
		if (cls.isAnnotationPresent(SkipProtoGenerator.class)) {
			return false;
		}

		if (cls.isEnum() && GeneratorProtoFeature.ENUM_TO_INT.prepare()) {
			return false;
		}

		if (cache.getCommonProtoTypes().contains(cls)) {
			return true;
		}

		GeneratorProtoCache.ClassProtoInfo protoInfo = cache.getClassProtoInfo(cls);
		if ( parentCommon
		 || cls.isAnnotationPresent(NeedProtoGenerator.class)
		 || cls.isAnnotationPresent(CommonModuleProto.class)
		) {
			currCommonProtoMessage = true;
			cache.addCommonType(cls);
		}

		if (cls.isEnum()) {
			return currCommonProtoMessage;
		}


		if (! handleCls.add(cls)) {
			// 在处理的. 都算common中去
			return true;
		}
		List<FieldInfo> fieldInfos = ProtobufProxyUtils.fetchFieldInfos(cls, false);
		for (FieldInfo fieldInfo : fieldInfos) {
			if (fieldInfo.getField().isAnnotationPresent(ProtoSubstitute.class)) {
				Class<?> type = fieldInfo.getField().getAnnotation(ProtoSubstitute.class).value();
				if (! ProtobufProxyUtils.isScalarType(type) && type != cls) {
					haveCommonProtoMessage |= recursiveObjClass(type, cache, currCommonProtoMessage, handleCls);
					protoInfo.addFieldInfo(type);
				}
				continue;
			}

			if (fieldInfo.isList()) {
				Class<?> type = ReflectUtil.getListGenericParameterizedType(fieldInfo.getField());
				if (! ProtobufProxyUtils.isScalarType(type) && type != cls) {
					haveCommonProtoMessage |= recursiveObjClass(type, cache, currCommonProtoMessage, handleCls);
					protoInfo.addFieldInfo(type);
				}
				continue;
			}

			if (fieldInfo.isMap()) {
				Class keyClass = fieldInfo.getGenericKeyType();
				Class valueClass = fieldInfo.getGenericeValueType();
				if (ProtobufProxyUtils.isObjectType(keyClass)) {
					haveCommonProtoMessage |= recursiveObjClass(keyClass, cache, currCommonProtoMessage, handleCls);
					protoInfo.addFieldInfo(keyClass);
				}

				if (ProtobufProxyUtils.isObjectType(valueClass) && valueClass != cls) {
					haveCommonProtoMessage |= recursiveObjClass(valueClass, cache, currCommonProtoMessage, handleCls);
					protoInfo.addFieldInfo(valueClass);
				}
				continue;
			}

			if (fieldInfo.isObjectType() || fieldInfo.getFieldType() == FieldType.ENUM) {
				haveCommonProtoMessage |= recursiveObjClass(fieldInfo.getField().getType(), cache, currCommonProtoMessage, handleCls);
				protoInfo.addFieldInfo(fieldInfo.getField().getType());
			}
		}
		return haveCommonProtoMessage | currCommonProtoMessage;
	}

	/**
	 * 输出 协议名 协议ID映射关系
	 * @param param
	 */
	public String protocolIdMapping(GeneratorProtoParam param) {
		if (! GeneratorProtoFeature.OUTPUT_PROTOCOL_LIST_ENUM.prepare()) {
			return "";
		}

		List<Class<?>> allPbClass = param.getAllPbClass();
		Map<String, Integer> protoReqIDEnum = Maps.newHashMapWithExpectedSize(allPbClass.size());
		protoReqIDEnum.put("NONE", 0);
		Map<String, Integer> protoRspIDEnum = Maps.newHashMapWithExpectedSize(allPbClass.size());
		protoRspIDEnum.put("NONE", 0);
		for (Class<?> pbClass : allPbClass) {
			int protocolId = getProtocolId(pbClass);
			if (protocolId != 0) {
				if (protocolId == IProtocolId.System.CLIENT_PING || ChannelDataMapping.getHandler(protocolId) != null) {
					protoReqIDEnum.put(pbClass.getSimpleName(), protocolId);
				}else {
					protoRspIDEnum.put(pbClass.getSimpleName(), protocolId);
				}
			}
		}
		StringBuilder code = new StringBuilder();
		this.generateProtocolIdMapping("ProtoReqId", "所有请求协议", code, protoReqIDEnum);
		this.generateProtocolIdMapping("ProtoRspId", "所有响应协议", code, protoRspIDEnum);
		return code.toString();
	}

	/**
	 * 生成markdown
	 * @param param
	 */
	public void createProtocolMappingMarkDown(GeneratorProtoParam param) {
		if (!GeneratorProtoFeature.OUTPUT_PROTOCOL_MAPPING_MD.prepare()) {
			return;
		}

		StringBuilder code = new StringBuilder();
		param.getGroupByModule().forEach((moduleName, list) -> {
			StringBuilder content = new StringBuilder("|协议ID|协议名|协议描述|KCP|\n|----|----|-----|----|\n");
			list.forEach(pbClass -> {
				ChannelData annotation = pbClass.getAnnotation(ChannelData.class);
				if (annotation == null) {
					return;
				}

				content.append("|").append(annotation.ID())
						.append("|").append(pbClass.getSimpleName())
						.append("|").append(annotation.desc())
						.append("|").append(annotation.kcp() ? "Yes" : "")
						.append("|\n");

			});
			code.append("\n## `").append(moduleName).append("`协议\n\n").append(content);
		});
		FileUtil.createFileWithContent(new File(param.getDirectory(), "ProtocolMapping.md"), "# 协议映射关系\n------\n" + code + "\n");
	}
}
