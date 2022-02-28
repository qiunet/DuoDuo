package org.qiunet.flash.handler.util.proto;

import com.baidu.bjf.remoting.protobuf.annotation.ProtobufClass;
import com.google.common.collect.Maps;
import org.qiunet.flash.handler.context.request.data.ChannelData;
import org.qiunet.utils.common.CommonUtil;
import org.qiunet.utils.exceptions.CustomException;
import org.qiunet.utils.file.FileUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;

/***
 *
 *
 * @author qiunet
 * 2020-09-23 16:43
 */
public enum ProtoGeneratorModel {
	/**
	 * 每个module 一个文件
	 */
	GROUP_BY_MODULE {
		@Override
		protected void consumePbClasses(GeneratorProtoParam param, BiConsumer<String, List<Class<?>>> consumer) {
			Map<String, List<Class<?>>> classes = Maps.newHashMapWithExpectedSize(param.getAllPbClass().size());
			param.getAllPbClass().forEach(cls -> {
				String moduleName;
				if (cls.getAnnotation(ChannelData.class).ID() < 1000) {
					moduleName = "System";
				}else {
					ProtoModule annotation = cls.getPackage().getAnnotation(ProtoModule.class);
					if (annotation == null) {
						throw new CustomException("Class {} not have @ProtoModule describe!", cls.getName());
					}

					if (CommonUtil.existInList(annotation.value(), "System", "__common__")) {
						throw new CustomException("@ProtoModule value can not be 'System' or '__common__' !");
					}

					moduleName = annotation.value();
				}

				classes.computeIfAbsent(moduleName, key -> new ArrayList<>()).add(cls);
			});
			classes.forEach(consumer);
		}

		@Override
		protected void startModule(GeneratorProtoParam param, StringBuilder code, List<Class<?>> moduleClasses) {
			boolean match = moduleClasses.stream().anyMatch(param::haveSubClass);
			code.replace(0, code.capacity(), param.getVersion().fileContentStringBuffer(match).toString());
		}

		@Override
		protected void endModule(GeneratorProtoParam param, String moduleName, StringBuilder code) {
			FileUtil.createFileWithContent(new File(param.getDirectory(), moduleName+".proto"), code.toString());
		}
	},
	/**
	 * 每个协议一个文件
	 */
	ONE_PROTOCOL_ONE_FILE {
		private String fileName(Class<?> cls) {
			ProtobufClass annotation = cls.getAnnotation(ProtobufClass.class);
			int protocolId = ProtoIDLGenerator.getProtocolId(cls);
			return protocolId+"_"+annotation.description()+".proto";
		}

		@Override
		protected void consumeClassContent(GeneratorProtoParam param, StringBuilder code, Class<?> cls, String idl) {
			code = param.getVersion().fileContentStringBuffer(param.haveSubClass(cls)).append(idl);
			FileUtil.createFileWithContent(new File(param.getDirectory(), fileName(cls)), code.toString());
		}
	},
	/**
	 * 都放在一起.
	 */
	ALL_IN_ONE {
		private static final String ALL_IN_ONE_FILE_NAME = "AllInOneProtobufProtocol.proto";

		@Override
		protected void endHandle(GeneratorProtoParam param, StringBuilder code) {
			FileUtil.createFileWithContent(new File(param.getDirectory(), ALL_IN_ONE_FILE_NAME), code.toString());
		}
	}
	;

	/**
	 * 开始生成proto文件
	 */
	public void generatorProto(GeneratorProtoParam param) {
		ProtoIDLGenerator protoIDLGenerator = param.getVersion().getProtoIDLGenerator();

		StringBuilder code = param.getVersion().fileContentStringBuffer(false);
		this.startHandle(code, param, protoIDLGenerator);

		this.consumePbClasses(param, (moduleName, pbClasses) -> {
			this.startModule(param, code, pbClasses);
			pbClasses.forEach(pbClass -> {
				this.consumeClassContent(param, code, pbClass, protoIDLGenerator.getIDL(pbClass));
			});
			this.endModule(param, moduleName, code);
		});
		this.endHandle(param, code);
	}

	/**
	 * 消费每次生成内容
	 * @param code
	 * @param param
	 * @param cls
	 */
	protected void consumeClassContent(GeneratorProtoParam param, StringBuilder code, Class<?> cls, String idl) {
		code.append(idl);
	}

	/**
	 * 起始处理 生成common.proto
	 */
	protected void startHandle(StringBuilder code, GeneratorProtoParam param, ProtoIDLGenerator protoIDLGenerator) {
		protoIDLGenerator.createProtocolMappingMarkDown(param);

		StringBuilder content = new StringBuilder(protoIDLGenerator.protocolIdMapping(param));
		protoIDLGenerator.generateCommonClassIDL(content, param.getSubClasses());

		if (this == ALL_IN_ONE) {
			code.append(content);
			return;
		}

		StringBuilder sb = param.getVersion().fileContentStringBuffer(false).append(content);
		FileUtil.createFileWithContent(new File(param.getDirectory(), ProtobufVersion.COMMON_CLASS_PROTO_FILE_NAME), sb.toString());
	}

	/**
	 * 将pbClasses 按照module group好.
	 * @param consumer
	 */
	protected void consumePbClasses(GeneratorProtoParam param, BiConsumer<String, List<Class<?>>> consumer) {
		consumer.accept(this.name(), param.getAllPbClass());
	}

	/**
	 * 结束处理 如果是all_in_one
	 */
	protected void endHandle(GeneratorProtoParam param, StringBuilder code){}

	/**
	 * 模块开始
	 * @param param
	 * @param code
	 */
	protected void startModule(GeneratorProtoParam param, StringBuilder code, List<Class<?>> moduleClasses){}

	/**
	 * 模块结束
	 * @param param
	 * @param code
	 */
	protected void endModule(GeneratorProtoParam param, String moduleName, StringBuilder code){}
}
