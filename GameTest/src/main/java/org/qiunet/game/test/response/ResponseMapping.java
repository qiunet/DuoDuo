package org.qiunet.game.test.response;

import com.google.common.collect.Maps;
import org.qiunet.flash.handler.context.request.data.IChannelData;
import org.qiunet.flash.handler.netty.server.param.adapter.message.StatusTipsResponse;
import org.qiunet.function.ai.node.IBehaviorAction;
import org.qiunet.utils.args.ArgsContainer;
import org.qiunet.utils.exceptions.CustomException;
import org.qiunet.utils.reflect.ReflectUtil;
import org.qiunet.utils.scanner.IApplicationContext;
import org.qiunet.utils.scanner.IApplicationContextAware;
import org.qiunet.utils.scanner.ScannerType;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;

/***
 * 测试用例里面response 扫描
 *
 * @author qiunet
 * 2021-07-07 15:15
 */
public class ResponseMapping implements IApplicationContextAware {

	private static final Map<Integer, Method> methodMapping = Maps.newHashMap();
	private static final Map<Integer, Method> statusMapping = Maps.newHashMap();

	@Override
	public void setApplicationContext(IApplicationContext context, ArgsContainer argsContainer) throws Exception {
		this.handlerStatusTips(context, argsContainer);
		this.handlerResponse(context);
	}

	private void handlerStatusTips(IApplicationContext context, ArgsContainer argsContainer) throws Exception{
		if (argsContainer.isNull(IStatusTipsHandler.STATUS_MAPPING_HANDLER)) {
			throw new CustomException("IStatusTipsHandler.STATUS_MAPPING_HANDLER need set!");
		}

		Function<Method, Set<Integer>> mapping = argsContainer.getArgument(IStatusTipsHandler.STATUS_MAPPING_HANDLER).get();
		context.getSubTypesOf(IStatusTipsHandler.class).forEach(clz -> {
			if (
					Modifier.isAbstract(clz.getModifiers())
				||  Modifier.isInterface(clz.getModifiers())
			) {
				return;
			}

			if (! IBehaviorAction.class.isAssignableFrom(clz)) {
				throw new CustomException("game.test status tips [clz:{}] need be a IBehaviorAction!", clz.getName());
			}

			Method mtd = null;
			try {
				mtd = clz.getMethod("statusHandler", StatusTipsResponse.class);
			} catch (NoSuchMethodException e) {
				e.printStackTrace();
			}
			if (mtd == null || mtd.getAnnotations().length == 0) {
				return;
			}

			Set<Integer> set = mapping.apply(mtd);
			if (set == null || set.isEmpty()) {
				return;
			}

			Method finalMtd = mtd;
			ReflectUtil.makeAccessible(mtd);
			set.forEach((status) -> {
				if (statusMapping.containsKey(status)) {
					throw new CustomException("game.test status tips [id:{}] is repeated!", status);
				}
				statusMapping.put(status, finalMtd);
			});
		});
	}

	private void handlerResponse(IApplicationContext context) {
		context.getMethodsAnnotatedWith(TestResponse.class).forEach(mtd -> {
			TestResponse annotation = mtd.getAnnotation(TestResponse.class);
			if (methodMapping.containsKey(annotation.value())) {
				throw new CustomException("game.test Response [id:{}] is repeated!", annotation.value());
			}

			if (! IBehaviorAction.class.isAssignableFrom(mtd.getDeclaringClass())) {
				throw new CustomException("Response [id:{}] need define in IBehaviorAction!", annotation.value());
			}

			if (mtd.getParameterCount() != 1) {
				throw new CustomException("Response [id:{}] just need a IChannelData parameter.", annotation.value());
			}

			if (! IChannelData.class.isAssignableFrom(mtd.getParameterTypes()[0])) {
				throw new CustomException("Response [id:{}] parameter need implement IChannelData.", annotation.value());
			}
			methodMapping.put(annotation.value(), mtd);
			ReflectUtil.makeAccessible(mtd);
		});
	}


	/**
	 * 获得statusId对应的方法
	 * @param statusId 状态id
	 * @return
	 */
	public static Method getStatusMethodByID(int statusId) {
		return statusMapping.get(statusId);
	}

	/**
	 * 获得responseID对应的方法
	 * @param responseId
	 * @return
	 */
	public static Method getResponseMethodByID(int responseId) {
		return methodMapping.get(responseId);
	}

	@Override
	public ScannerType scannerType() {
		return ScannerType.TESTER;
	}
}
