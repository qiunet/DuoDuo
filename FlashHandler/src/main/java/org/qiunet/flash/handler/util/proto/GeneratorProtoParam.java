package org.qiunet.flash.handler.util.proto;

import com.google.common.collect.ComparisonChain;
import com.google.common.collect.Maps;
import org.qiunet.flash.handler.context.request.data.ChannelData;
import org.qiunet.flash.handler.context.request.data.IChannelData;
import org.qiunet.utils.async.LazyLoader;
import org.qiunet.utils.common.CommonUtil;
import org.qiunet.utils.exceptions.CustomException;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/***
 *
 * @author qiunet
 * 2022/2/9 17:41
 */
public class GeneratorProtoParam extends GeneratorProtoCache {
	private final LazyLoader<Map<String, List<Class<?>>>> groupByModule = new LazyLoader<>(this::groupByModule);
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
				.sorted((o1, o2) -> ComparisonChain.start().compare(o1.getSimpleName(), o2.getSimpleName()).result())
				.collect(Collectors.toList());
	}

	public ProtoGeneratorModel getModel() {
		return model;
	}

	public List<Class<?>> getAllPbClass() {
		return allPbClass;
	}

	public Map<String, List<Class<?>>> getGroupByModule() {
		return groupByModule.get();
	}

	/**
	 * 根据module分组
	 * @return
	 */
	private Map<String, List<Class<?>>> groupByModule() {
		Map<String, List<Class<?>>> groupByModule = Maps.newHashMapWithExpectedSize(allPbClass.size());
		allPbClass.forEach(cls -> {
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

			groupByModule.computeIfAbsent(moduleName, key -> new ArrayList<>()).add(cls);
		});
		return groupByModule;
	}

	public File getDirectory() {
		return directory;
	}
}
