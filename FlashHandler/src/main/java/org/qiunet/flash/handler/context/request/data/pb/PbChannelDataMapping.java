package org.qiunet.flash.handler.context.request.data.pb;

import com.baidu.bjf.remoting.protobuf.annotation.ProtobufClass;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import net.bytebuddy.ByteBuddy;
import net.bytebuddy.agent.ByteBuddyAgent;
import net.bytebuddy.description.annotation.AnnotationDescription;
import net.bytebuddy.dynamic.loading.ClassReloadingStrategy;
import org.qiunet.flash.handler.common.annotation.UriPathHandler;
import org.qiunet.flash.handler.handler.IHandler;
import org.qiunet.utils.args.ArgsContainer;
import org.qiunet.utils.collection.DuMap;
import org.qiunet.utils.exceptions.CustomException;
import org.qiunet.utils.logger.LoggerType;
import org.qiunet.utils.reflect.ReflectUtil;
import org.qiunet.utils.scanner.IApplicationContext;
import org.qiunet.utils.scanner.IApplicationContextAware;
import org.slf4j.Logger;

import java.lang.reflect.Modifier;
import java.util.Map;
import java.util.Set;

/***
 * Protobuf data Id 和 类的映射关系.
 *
 * @author qiunet
 * 2020-09-22 12:50
 */
public class PbChannelDataMapping implements IApplicationContextAware {
	private IApplicationContext context;
	private static final Logger logger = LoggerType.DUODUO_FLASH_HANDLER.getLogger();
	/**
	 * 请求ID 和 handler 的映射关系
	 */
	private static final Map<Integer, IHandler> handlerMapping = Maps.newHashMap();
	/**
	 * pbChannelData Class 和 Id的映射关系
	 */
	private static final DuMap<Class<? extends IpbChannelData>, Integer> mapping = new DuMap();

	private PbChannelDataMapping(){}

	@Override
	public void setApplicationContext(IApplicationContext context, ArgsContainer argsContainer) throws Exception {
		this.context = context;
		this.handlerPbChannelData(context);
		this.requestHandlerProcess(context);
	}

	/**
	 * 请求的处理
	 * @param context
	 */
	private void requestHandlerProcess(IApplicationContext context) {
		context.getSubTypesOf(IHandler.class).stream()
				.filter(c -> ! Modifier.isAbstract(c.getModifiers()))
				.filter(c -> ! Modifier.isInterface(c.getModifiers()))
				.filter(c -> ! c.isAnnotationPresent(UriPathHandler.class))
				.forEach(this::addHandler);
	}
	/**
	 * 处理pb Channel data
	 * @param context
	 */
	private void handlerPbChannelData(IApplicationContext context) {
		Set<Integer> protocolIds = Sets.newHashSet();
		ByteBuddyAgent.install();
		for (Class<? extends IpbChannelData> clazz : context.getSubTypesOf(IpbChannelData.class)) {
			if (Modifier.isAbstract(clazz.getModifiers())
					|| Modifier.isInterface(clazz.getModifiers())
			) {
				continue;
			}

			if (! clazz.isAnnotationPresent(PbChannelData.class)) {
				throw new IllegalArgumentException("Class ["+clazz.getName()+"] is not specify PbChannelDataID annotation!");
			}

			PbChannelData pbChannelData = clazz.getAnnotation(PbChannelData.class);
			if (protocolIds.contains(pbChannelData.ID())) {
				throw new IllegalArgumentException("Class ["+clazz.getName()+"] specify protocol value ["+ pbChannelData.ID()+"] is repeated!");
			}

			new ByteBuddy().redefine(clazz).annotateType(
					AnnotationDescription.Builder.ofType(ProtobufClass.class)
							.define("description", pbChannelData.desc())
							.build()
			).make()
					.load(clazz.getClassLoader(), ClassReloadingStrategy.fromInstalledAgent());

			protocolIds.add(pbChannelData.ID());
			mapping.put(clazz, pbChannelData.ID());
		}
	}

	@Override
	public int order() {
		return Integer.MAX_VALUE;
	}

	public static int protocolId(Class<? extends IpbChannelData> clazz) {
		return mapping.getVal(clazz);
	}

	public static Class<? extends IpbChannelData> protocolClass(int protocolId) {
		return mapping.getKey(protocolId);
	}

	public static IHandler getHandler(int protocolId) {
		return handlerMapping.get(protocolId);
	}
	/**
	 * 存一个handler对应mapping
	 * @param handlerClz
	 */
	void addHandler(Class<? extends IHandler> handlerClz) {
		Class<? extends IpbChannelData> type = (Class<? extends IpbChannelData>) ReflectUtil.findGenericParameterizedType(handlerClz, IpbChannelData.class::isAssignableFrom);
		if (! mapping.containsKey(type)) {
			throw new CustomException("IHandler ["+handlerClz.getSimpleName()+"] can not get IPbChannelData info!");
		}
		int protocolId = protocolId(type);
		if (handlerMapping.containsKey(protocolId)) {
			throw new CustomException("protocolId ["+protocolId+"] className ["+handlerClz.getSimpleName()+"] is already exist!");
		}

		IHandler handler = (IHandler) this.context.getInstanceOfClass(handlerClz);
		ReflectUtil.setField(handler, "requestDataClass", type);
		ReflectUtil.setField(handler, "protocolId", protocolId);
		logger.info("ProtocolID [{}] RequestHandler [{}] was found and mapping.", protocolId, handler.getClass().getSimpleName());
		handlerMapping.put(protocolId, handler);
	}
}
