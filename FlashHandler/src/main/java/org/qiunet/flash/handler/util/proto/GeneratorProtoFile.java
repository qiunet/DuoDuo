package org.qiunet.flash.handler.util.proto;

import com.google.common.base.Preconditions;
import com.google.common.collect.ComparisonChain;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import org.qiunet.flash.handler.context.request.data.ChannelData;
import org.qiunet.flash.handler.context.request.data.IChannelData;
import org.qiunet.utils.args.ArgsContainer;
import org.qiunet.utils.data.ArgsData;
import org.qiunet.utils.exceptions.CustomException;
import org.qiunet.utils.scanner.IApplicationContext;
import org.qiunet.utils.scanner.IApplicationContextAware;
import org.qiunet.utils.scanner.ScannerType;

import java.io.File;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/***
 * 生成proto 文件给客户端.
 *
 * @author qiunet
 * 2020-09-23 16:34
 */
public class GeneratorProtoFile implements IApplicationContextAware {
	// 如果使用server.conf配置, 协议存放目录
	public static final String PROTO_OUTPUT_DIR = "proto_output_dir";

	private static final List<Class<?>> classes = Lists.newArrayList();

	private GeneratorProtoFile(){}
	/**
	 * 生成协议文件
	 * @param directory 生成的目录
	 * @param model 生成类型
	 * @throws Exception -
	 */
	public static void generator(File directory, ProtoGeneratorModel model, ArgsData.Two<GeneratorProtoFeature, Object> ... features) throws Exception {
		generator(directory, model, ProtobufVersion.V3, features);
	}
	/**
	 * 生成协议文件
	 * 兼容测试需要feature {@link GeneratorProtoFeature#COMPATIBLE_CHECK}
	 * @param directory 生成的目录
	 * @param model 生成类型
	 * @throws Exception -
	 */
	public static void generator(File directory, ProtoGeneratorModel model, ProtobufVersion version, ArgsData.Two<GeneratorProtoFeature, Object>... features) throws Exception {
		if (! directory.exists()) {
			if (! directory.mkdirs()) {
				throw new CustomException("mkdir {} error", directory.getAbsolutePath());
			}
		}
		Preconditions.checkState(directory.isDirectory(), "Directory must be a directory!");
		Preconditions.checkState(model != null, "model is null");
		GeneratorProtoFeature.features.clear();

		GeneratorProtoFeature.features.putAll(Arrays.stream(features).collect(Collectors.toMap(ArgsData.Two::a, ArgsData.Two::b)));
		boolean compatibleCheck = GeneratorProtoFeature.COMPATIBLE_CHECK.prepare();
		ProtoCompatible protoCompatible = null;
		if (compatibleCheck) {
			protoCompatible = new ProtoCompatible(directory);
		}
		GeneratorProtoParam protoParam = new GeneratorProtoParam(model, classes, version, directory);
		model.generatorProto(protoParam);
		if (compatibleCheck && !protoCompatible.compatible(new ProtoCompatible(directory))) {
			throw new ProtocolUnCompatibleException("======协议不兼容之前的版本. 详情请查看上面打印! 如果确认无误. 需要你手动提交版本管理! =========");
		}
	}

	@Override
	public void setApplicationContext(IApplicationContext context, ArgsContainer argsContainer) throws Exception {
		Set<Class<?>> classes = Sets.newHashSet();
		classes.addAll(context.getTypesAnnotatedWith(NeedProtoGenerator.class));
		classes.addAll(context.getSubTypesOf(IChannelData.class));

		List<Class<?>> collect = classes.stream()
			.filter(clz -> !Modifier.isInterface(clz.getModifiers()))
			.filter(clz -> ! clz.isAnnotationPresent(SkipProtoGenerator.class))
			.filter(clz -> !Modifier.isAbstract(clz.getModifiers()))
				.sorted((o1, o2) -> {
					int protocolId1 = o1.isAnnotationPresent(ChannelData.class) ? o1.getAnnotation(ChannelData.class).ID() : 0;
					int protocolId2 = o2.isAnnotationPresent(ChannelData.class) ? o2.getAnnotation(ChannelData.class).ID() : 0;
					return ComparisonChain.start().compare(protocolId2, protocolId1).result();
				})
			.toList();

		GeneratorProtoFile.classes.addAll(collect);
	}

	@Override
	public ScannerType scannerType() {
		return ScannerType.GENERATOR_PROTO;
	}
}
