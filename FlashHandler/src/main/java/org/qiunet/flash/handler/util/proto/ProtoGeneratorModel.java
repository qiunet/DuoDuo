package org.qiunet.flash.handler.util.proto;

import com.baidu.bjf.remoting.protobuf.annotation.ProtobufClass;
import org.qiunet.utils.file.FileUtil;

import java.io.File;

/***
 *
 *
 * @author qiunet
 * 2020-09-23 16:43
 */
public enum ProtoGeneratorModel {
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
		protected void consumeClassContent(GeneratorProtoParam param, StringBuilder code, Class<?> cls, String idl) {
			code.append(idl);
		}

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

		for (Class<?> pbClass : param.getAllPbClass()) {
			if (pbClass.isAnnotationPresent(SkipProtoGenerator.class)
			 || pbClass.isAnnotationPresent(NeedProtoGenerator.class)) {
				continue;
			}

			this.consumeClassContent(param, code, pbClass, protoIDLGenerator.getIDL(pbClass));
		}

		this.endHandle(param, code);
	}

	/**
	 * 消费每次生成内容
	 * @param code
	 * @param param
	 * @param cls
	 */
	protected abstract void consumeClassContent(GeneratorProtoParam param, StringBuilder code, Class<?> cls, String idl);

	/**
	 * 起始处理 生成common.proto
	 */
	protected void startHandle(StringBuilder code, GeneratorProtoParam param, ProtoIDLGenerator protoIDLGenerator) {
		StringBuilder content = new StringBuilder(protoIDLGenerator.protocolIdMapping(param.getAllPbClass()));
		protoIDLGenerator.generateCommonClassIDL(content, param.getSubClasses());

		if (this == ALL_IN_ONE) {
			code.append(content);
			return;
		}

		StringBuilder sb = param.getVersion().fileContentStringBuffer(false).append(content);
		FileUtil.createFileWithContent(new File(param.getDirectory(), ProtobufVersion.COMMON_CLASS_PROTO_FILE_NAME), sb.toString());
	}

	/**
	 * 结束处理 如果是all_in_one
	 */
	protected void endHandle(GeneratorProtoParam param, StringBuilder code){}
}
