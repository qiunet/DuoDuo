package org.qiunet.flash.handler.common.enums;

import com.baidu.bjf.remoting.protobuf.annotation.ProtobufClass;
import com.google.common.collect.Sets;
import org.qiunet.flash.handler.util.ProtobufIDLGenerator;
import org.qiunet.flash.handler.util.SkipProtoGenerator;
import org.qiunet.utils.file.FileUtil;

import java.io.File;
import java.util.List;
import java.util.Set;
import java.util.StringJoiner;

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
		@Override
		public void generatorProto(File directory, List<Class<?>> allPbClass) {
			for (Class<?> pbClass : allPbClass) {
				if (pbClass.isAnnotationPresent(SkipProtoGenerator.class)) {
					continue;
				}

				ProtobufClass annotation = pbClass.getAnnotation(ProtobufClass.class);
				int protocolId = ProtobufIDLGenerator.getProtocolId(pbClass);
				StringJoiner sb = new StringJoiner("_", "", ".proto");

				sb.add(annotation.description());
				if (protocolId > 0) sb.add(""+protocolId);
				sb.add(pbClass.getSimpleName());

				String content = ProtoGeneratorModel.generatorProtoContent(pbClass, null, null, false);
				FileUtil.createFileWithContent(new File(directory, sb.toString()), content);
			}
		}
	},
	/**
	 * 都放在一起.
	 */
	ALL_IN_ONE {
		@Override
		public void generatorProto(File directory, List<Class<?>> allPbClass) {
			Set<Class<?>> cachedEnumsTypes = Sets.newHashSet();
			Set<Class<?>> cachedTypes = Sets.newHashSet();

			StringBuilder sb = new StringBuilder(V3_HEADER);
			sb.append("\n\n");
			for (Class<?> pbClass : allPbClass) {
				if (pbClass.isAnnotationPresent(SkipProtoGenerator.class)) {
					continue;
				}

				String content = ProtoGeneratorModel.generatorProtoContent(pbClass, cachedEnumsTypes, cachedTypes, true);
				sb.append(content);
			}
			FileUtil.createFileWithContent(new File(directory, "AllInOneProtobufProtocol.proto"), sb.toString());
		}
	},
	;
	/** The Constant V3_HEADER. */
	private static final String V3_HEADER = "syntax=\"proto3\";\n";

	/**
	 * 开始生成proto文件
	 */
	public abstract void generatorProto(File directory, List<Class<?>> allPbClass);

	private static String generatorProtoContent(Class<?> clazz, Set<Class<?>> cachedEnumsTypes, Set<Class<?>> cachedTypes, boolean ignoreVersion) {
		return ProtobufIDLGenerator.getIDL(clazz, cachedTypes, cachedEnumsTypes, true, ignoreVersion);
	}
}
