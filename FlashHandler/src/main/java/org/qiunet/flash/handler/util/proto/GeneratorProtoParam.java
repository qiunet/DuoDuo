package org.qiunet.flash.handler.util.proto;

import org.qiunet.flash.handler.context.request.data.IChannelData;

import java.io.File;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/***
 *
 * @author qiunet
 * 2022/2/9 17:41
 */
public class GeneratorProtoParam {
	/**
	 * 某个class是否需要引用common.proto
	 */
	private final Set<Class<?>> haveSubClass = new HashSet<>();
	/**
	 * 有子类型的类
	 */
	private final Set<Class<?>> subClasses = new HashSet<>();
	/**
	 * 模式
	 */
	private final ProtoGeneratorModel model;
	/**
	 * 所有需要generator proto的 IChannelData类
	 */
	private final List<Class<?>> allPbClass;
	/**
	 * 输出版本
	 */
	private final ProtobufVersion version;
	/**
	 * 输出文件夹
	 */
	private final File directory;

	public GeneratorProtoParam(ProtoGeneratorModel model, List<Class<?>> allClass, ProtobufVersion version, File directory) {
		this.directory = directory;
		this.version = version;
		this.model = model;

		for (Class<?> aClass : allClass) {
			if (ProtoIDLGenerator.recursiveObjClass(aClass, subClasses)) {
				haveSubClass.add(aClass);
			}
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

	public ProtobufVersion getVersion() {
		return version;
	}

	public File getDirectory() {
		return directory;
	}

	public Set<Class<?>> getSubClasses() {
		return subClasses;
	}

	public boolean haveSubClass(Class<?> cls) {
		return haveSubClass.contains(cls);
	}
}
