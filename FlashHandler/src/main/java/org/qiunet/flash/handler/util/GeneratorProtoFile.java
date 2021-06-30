package org.qiunet.flash.handler.util;

import com.baidu.bjf.remoting.protobuf.utils.ProtobufProxyUtils;
import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import org.qiunet.flash.handler.common.enums.ProtoGeneratorModel;
import org.qiunet.flash.handler.context.request.data.pb.IpbChannelData;
import org.qiunet.utils.args.ArgsContainer;
import org.qiunet.utils.scanner.ClassScanner;
import org.qiunet.utils.scanner.IApplicationContext;
import org.qiunet.utils.scanner.IApplicationContextAware;
import org.qiunet.utils.scanner.ScannerType;

import java.io.File;
import java.lang.reflect.Modifier;
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
	private static final List<Class<?>> pbClasses = Lists.newArrayList();
	static {
		ProtobufProxyUtils.FIELD_FILTER_STARTS.add("$");
		ProtobufProxyUtils.FIELD_FILTER_STARTS.add("_");
	}
	private GeneratorProtoFile(){}
	/**
	 *
	 * @param directory 生成proto存放的文件夹
	 */
	public static void generator(File directory, ProtoGeneratorModel model, String packetPrefix) throws Exception {
		Preconditions.checkState(directory != null && directory.isDirectory(), "Directory must be a directory!");
		Preconditions.checkState(model != null, "model is null");
		ClassScanner.getInstance(ScannerType.SERVER).scanner(packetPrefix);
		model.generatorProto(directory, pbClasses);
	}

	@Override
	public void setApplicationContext(IApplicationContext context, ArgsContainer argsContainer) throws Exception {
		Set<Class<?>> classes = Sets.newHashSet();
		classes.addAll(context.getTypesAnnotatedWith(NeedProtoGenerator.class));
		classes.addAll(context.getSubTypesOf(IpbChannelData.class));

		Set<Class<?>> collect = classes.stream()
			.filter(clz -> !Modifier.isInterface(clz.getModifiers()))
			.filter(clz -> !Modifier.isAbstract(clz.getModifiers()))
			.collect(Collectors.toSet());

		pbClasses.addAll(collect);
	}

	@Override
	public ScannerType scannerType() {
		return ScannerType.SERVER;
	}
}
