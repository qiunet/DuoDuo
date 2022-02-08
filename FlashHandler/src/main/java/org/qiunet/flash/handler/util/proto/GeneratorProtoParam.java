package org.qiunet.flash.handler.util.proto;

import java.io.File;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
	 * 所有需要generator proto的类
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

	public GeneratorProtoParam(List<Class<?>> allPbClass, ProtobufVersion version, File directory) {
		this.allPbClass = allPbClass;
		this.directory = directory;
		this.version = version;

		for (Class<?> aClass : allPbClass) {
			if (ProtoIDLGenerator.recursiveObjClass(aClass, subClasses)) {
				haveSubClass.add(aClass);
			}
		}
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
