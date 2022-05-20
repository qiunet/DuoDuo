package org.qiunet.flash.handler.util.proto;

import org.qiunet.flash.handler.context.request.data.IChannelData;

import java.io.File;
import java.util.List;
import java.util.stream.Collectors;

/***
 *
 * @author qiunet
 * 2022/2/9 17:41
 */
public class GeneratorProtoParam extends GeneratorProtoCache {
	/**
	 * 模式
	 */
	private final ProtoGeneratorModel model;
	/**
	 * 所有需要generator proto的 IChannelData类
	 */
	private final List<Class<?>> allPbClass;
	/**
	 * 输出文件夹
	 */
	private final File directory;

	public GeneratorProtoParam(ProtoGeneratorModel model, List<Class<?>> allClass, ProtobufVersion version, File directory) {
		super(version);

		this.directory = directory;
		this.model = model;

		for (Class<?> aClass : allClass) {
			ProtoIDLGenerator.recursiveObjClass(aClass, this);
		}

		this.allPbClass = allClass.stream()
				.filter(IChannelData.class::isAssignableFrom)
				.collect(Collectors.toList());
	}

	public ProtoGeneratorModel getModel() {
		return model;
	}

	public List<Class<?>> getAllPbClass() {
		return allPbClass;
	}

	public File getDirectory() {
		return directory;
	}
}
