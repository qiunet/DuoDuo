package org.qiunet.flash.handler.util.proto;

import com.baidu.bjf.remoting.protobuf.annotation.ProtobufClass;
import com.google.common.collect.Lists;
import org.qiunet.utils.file.FileUtil;

import java.io.File;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
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
			param.getGroupByModule().forEach(consumer);
		}

		@Override
		protected void consumeClassContent(GeneratorProtoParam param, StringBuilder code, Class<?> cls) {
			// nothing
		}

		@Override
		protected void startModule(GeneratorProtoParam param, String moduleName, StringBuilder code, List<Class<?>> moduleClasses) {
			StringBuilder content = new StringBuilder();
			boolean match = param.generator.generatorIDLs(param, moduleName, content, false, moduleClasses);
			code.replace(0, code.capacity(), param.getVersion().fileContentStringBuffer(match).toString());
			code.append("\n").append(content);
		}

		@Override
		protected void endModule(GeneratorProtoParam param, String moduleName, StringBuilder code) {
			String fileContent = code.toString();
			outputFiles.add(() -> {
				FileUtil.createFileWithContent(new File(param.getDirectory(), moduleName + ".proto"), fileContent);
			});
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
		protected void consumeClassContent(GeneratorProtoParam param, StringBuilder code, Class<?> cls) {
			StringBuilder content = new StringBuilder();
			String fileName = fileName(cls);
			boolean haveCommonProto = param.generator.generatorIDLs(param, fileName, content, false, cls);
			code = param.getVersion().fileContentStringBuffer(haveCommonProto);
			code.append("\n").append(content);

			String fileContent = code.toString();
			outputFiles.add(() -> {
				FileUtil.createFileWithContent(new File(param.getDirectory(), fileName), fileContent);
			});
		}
	},
	/**
	 * 都放在一起.
	 */
	ALL_IN_ONE {
		private static final String ALL_IN_ONE_FILE_NAME = "AllInOneProtobufProtocol.proto";

		@Override
		protected void prepareHandler(StringBuilder code, GeneratorProtoParam param) {
			StringBuilder content = new StringBuilder(param.generator.protocolIdMapping(param));
			Set<Class<?>> classes = new HashSet<>(param.getCommonProtoTypes());
			classes.addAll(param.getAllPbClass());

			param.generator.generatorIDLs(param, ALL_IN_ONE_FILE_NAME, content, true, classes);
			code.append(content);
		}

		@Override
		protected void consumeClassContent(GeneratorProtoParam param, StringBuilder code, Class<?> cls) {}

		@Override
		protected void endHandle(GeneratorProtoParam param, StringBuilder code) {
			String fileContent = code.toString();
			outputFiles.add(() -> {
				FileUtil.createFileWithContent(new File(param.getDirectory(), ALL_IN_ONE_FILE_NAME), fileContent);
			});
		}
	}
	;
	protected final List<Runnable> outputFiles = Lists.newArrayListWithCapacity(100);
	/**
	 * 开始生成proto文件
	 */
	public void generatorProto(GeneratorProtoParam param) {
		param.generator.createProtocolMappingMarkDown(param);

		StringBuilder code = param.getVersion().fileContentStringBuffer(false);
		this.prepareHandler(code, param);

		this.consumePbClasses(param, (moduleName, pbClasses) -> {
			this.startModule(param, moduleName, code, pbClasses);
			pbClasses.forEach(pbClass -> {
				this.consumeClassContent(param, code, pbClass);
			});
			this.endModule(param, moduleName, code);
		});
		this.endHandle(param, code);

		param.generator.analyseDepends();
		outputFiles.forEach(Runnable::run);
	}

	/**
	 * 消费每次生成内容
	 * @param code
	 * @param param
	 * @param cls
	 */
	protected abstract void consumeClassContent(GeneratorProtoParam param, StringBuilder code, Class<?> cls);

	/**
	 * 起始处理 生成common.proto
	 */
	protected void prepareHandler(StringBuilder code, GeneratorProtoParam param) {
		StringBuilder content = new StringBuilder(param.generator.protocolIdMapping(param));
		param.generator.generatorIDLs(param, ProtobufVersion.COMMON_CLASS_PROTO_FILE_NAME, content, true, param.getCommonProtoTypes());

		StringBuilder sb = param.getVersion().fileContentStringBuffer(false).append(content);
		String fileContent = sb.toString();
		outputFiles.add(() -> {
			FileUtil.createFileWithContent(new File(param.getDirectory(), ProtobufVersion.COMMON_CLASS_PROTO_FILE_NAME), fileContent);
		});
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
	protected void startModule(GeneratorProtoParam param, String moduleName, StringBuilder code, List<Class<?>> moduleClasses){}

	/**
	 * 模块结束
	 * @param param
	 * @param code
	 */
	protected void endModule(GeneratorProtoParam param, String moduleName, StringBuilder code){}
}
