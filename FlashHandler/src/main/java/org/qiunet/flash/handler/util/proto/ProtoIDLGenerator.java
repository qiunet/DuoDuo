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
import org.qiunet.flash.handler.context.request.data.ChannelData;
import org.qiunet.flash.handler.context.request.data.ChannelDataMapping;
import org.qiunet.flash.handler.context.request.data.IChannelData;
import org.qiunet.utils.exceptions.CustomException;
import org.qiunet.utils.reflect.ReflectUtil;
import org.qiunet.utils.string.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
import java.util.Set;

/***
 *
 * @author qiunet
 * 2022/2/9 09:45
 */
public class ProtoIDLGenerator {
	static {
		ProtobufProxyUtils.FIELD_FILTER_STARTS.add("$");
		ProtobufProxyUtils.FIELD_FILTER_STARTS.add("_");
	}

	/** Logger for this class. */
	protected static final Logger LOGGER = LoggerFactory.getLogger(ProtoIDLGenerator.class.getName());

	protected final ProtobufVersion version;

	public ProtoIDLGenerator(ProtobufVersion version) {
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

		if (! cls.isAnnotationPresent(ChannelData.class) && ! cls.isAnnotationPresent(NeedProtoGenerator.class)) {
			throw new CustomException("Class {} not a channel data or NeedProtoGenerator class", cls.getName());
		}

		StringBuilder code = new StringBuilder();
		this.generateIDL(code, cls);
		return code.toString();
	}

	/**
	 * 生成普通obj的
	 * @param code
	 * @param cls
	 */
	protected void generateIDL(StringBuilder code, Class<?> cls) {
		code.append(generatorComment(cls)).append("message ").append(cls.getSimpleName()).append(" {  \n");

		List<FieldInfo> fieldInfos = ProtobufProxyUtils.fetchFieldInfos(cls, false);
		boolean isMap = false;
		for (FieldInfo field : fieldInfos) {
			if (field.hasDescription()) {
				code.append("// ").append(field.getDescription()).append("\n");
			}
			if (field.getFieldType() == FieldType.OBJECT || field.getFieldType() == FieldType.ENUM) {
				if (field.isList()) {
					Class<?> c = ReflectUtil.getListGenericParameterizedType(field.getField());
					if (c != null) {
						String fieldTypeName = c.getSimpleName();
						if (ProtobufProxyUtils.isScalarType(c)) {
							FieldType fieldType = ProtobufProxyUtils.TYPE_MAPPING.get(c);
							fieldTypeName = fieldType.getType();
						}
						code.append("repeated ").append(fieldTypeName).append(" ")
								.append(field.getField().getName()).append("=").append(field.getOrder())
								.append(version.getRepeatedFieldDescribe(c)).append(";\n");
					}
				} else {
					Class<?> c = field.getField().getType();
					code.append(version.getFieldDescribe()).append(" ").append(c.getSimpleName()).append(" ")
							.append(field.getField().getName()).append("=").append(field.getOrder()).append(";\n");
				}
			} else {
				String type = field.getFieldType().getType().toLowerCase();

				if (field.getFieldType() == FieldType.ENUM) {
					// if enum type
					Class c = field.getField().getType();
					if (Enum.class.isAssignableFrom(c)) {
						type = c.getSimpleName();
					}
				} else if (field.getFieldType() == FieldType.MAP) {
					isMap = true;
					Class keyClass = field.getGenericKeyType();
					Class valueClass = field.getGenericeValueType();
					type = type + "<" + ProtobufProxyUtils.processProtobufType(keyClass) + ", ";
					type = type + ProtobufProxyUtils.processProtobufType(valueClass)  + ">";
				}

				String required = version.getFieldDescribe();
				if (isMap) {
					required = "";
				}

				if (field.isList()) {
					required = "repeated";
				}

				code.append(required).append(" ").append(type).append(" ").append(field.getField().getName()).append("=").append(field.getOrder());
				if (required.equals("repeated"))code.append(version.getRepeatedFieldDescribe(field.getGenericKeyType()));
				code.append(";\n");
			}
		}

		code.append("}\n");
		code.append("\n\n");
	}
	/**
	 * 生成枚举的
	 * @param code
	 * @param cls
	 */
	protected void generateEnumIDL(StringBuilder code, Class<Enum> cls) {
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

	/**
	 * 生成其它共用的class proto信息
	 * @param code
	 * @param subClassSet
	 */
	public void generateCommonClassIDL(StringBuilder code, Set<Class<?>> subClassSet) {
		subClassSet.stream()
		.filter(cls -> ! cls.isAnnotationPresent(SkipProtoGenerator.class))
		.sorted(((o1, o2) -> ComparisonChain.start().compare(o1.getSimpleName(), o2.getSimpleName()).result()))
		.forEach(cls -> {
			if (cls.isEnum()) {
				generateEnumIDL(code, (Class<Enum>) cls);
			}else {
				generateIDL(code, cls);
			}
		});
	}

	/**
	 *
	 * @param clz
	 * @return
	 */
	protected String generatorComment(Class<?> clz) {
		ProtobufClass annotation = clz.getAnnotation(ProtobufClass.class);
		if (annotation == null) {
			return "";
		}

		StringBuilder sb = new StringBuilder();
		sb.append("// ");
		if (IChannelData.class.isAssignableFrom(clz)) {
			int protocolId = getProtocolId(clz);
			sb.append("[ProtocolId = ").append(protocolId).append(" ] ");
		}
		sb.append(annotation.description()).append("\n");
		return sb.toString();
	}

	/**
	 * 获取 IChannelData 里面的所有其它非自有类型
	 * @param cls
	 * @return 是否包含 common message
	 */
	public static boolean recursiveObjClass(Class<?> cls, Set<Class<?>> classCache) {
		if (cls.isAnnotationPresent(SkipProtoGenerator.class)) {
			return false;
		}

		if (cls.isAnnotationPresent(NeedProtoGenerator.class)) {
			classCache.add(cls);
		}

		if (cls.isEnum()) {
			classCache.add(cls);
			return false;
		}

		List<FieldInfo> fieldInfos = ProtobufProxyUtils.fetchFieldInfos(cls, false);
		boolean haveCommonProtoMessage = false;
		for (FieldInfo fieldInfo : fieldInfos) {
			if (fieldInfo.isList()) {
				Class<?> type = ReflectUtil.getListGenericParameterizedType(fieldInfo.getField());
				if (! ProtobufProxyUtils.isScalarType(type) && classCache.add(type)) {
					recursiveObjClass(type, classCache);
				}

				if (!ProtobufProxyUtils.isScalarType(type)) {
					haveCommonProtoMessage = true;
				}
				continue;
			}

			if (fieldInfo.isMap()) {
				Class keyClass = fieldInfo.getGenericKeyType();
				Class valueClass = fieldInfo.getGenericeValueType();
				if (ProtobufProxyUtils.isObjectType(keyClass) && classCache.add(keyClass)) {
					recursiveObjClass(keyClass, classCache);
				}

				if (ProtobufProxyUtils.isObjectType(valueClass) && classCache.add(valueClass)) {
					recursiveObjClass(valueClass, classCache);
				}

				if (ProtobufProxyUtils.isObjectType(keyClass)
						|| ProtobufProxyUtils.isObjectType(valueClass)) {
					haveCommonProtoMessage = true;
				}
				continue;
			}

			if (fieldInfo.isObjectType() || fieldInfo.getFieldType() == FieldType.ENUM) {
				if (classCache.add(fieldInfo.getField().getType())) {
					recursiveObjClass(fieldInfo.getField().getType(), classCache);
				}
				haveCommonProtoMessage = true;
			}
		}
		return haveCommonProtoMessage;
	}

	/**
	 * 输出 协议名 协议ID映射关系
	 * @param allPbClass
	 */
	public String protocolIdMapping(List<Class<?>> allPbClass) {
		StringBuilder code = new StringBuilder();

		Map<String, Integer> protoReqIDEnum = Maps.newHashMapWithExpectedSize(allPbClass.size());
		protoReqIDEnum.put("ProtoReqId_NONE", 0);
		Map<String, Integer> protoRspIDEnum = Maps.newHashMapWithExpectedSize(allPbClass.size());
		protoRspIDEnum.put("ProtoRspId_NONE", 0);
		for (Class<?> pbClass : allPbClass) {
			if (pbClass.isAnnotationPresent(SkipProtoGenerator.class)) {
				continue;
			}

			int protocolId = getProtocolId(pbClass);
			if (protocolId != 0) {
				if (ChannelDataMapping.getHandler(protocolId) != null) {
					protoReqIDEnum.put(pbClass.getSimpleName(), protocolId);
				}else {
					protoRspIDEnum.put(pbClass.getSimpleName(), protocolId);
				}
			}
		}

		this.generateProtocolIdMapping("ProtoReqId", "所有请求协议", code, protoReqIDEnum);
		this.generateProtocolIdMapping("ProtoRspId", "所有响应协议", code, protoRspIDEnum);
		return code.toString();
	}
}
