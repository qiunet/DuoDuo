package org.qiunet.game.test.response;

import com.google.common.collect.Maps;
import org.qiunet.flash.handler.context.request.data.pb.IpbChannelData;
import org.qiunet.flash.handler.netty.server.param.adapter.message.StatusTipsResponse;
import org.qiunet.function.ai.node.IBehaviorAction;
import org.qiunet.utils.args.ArgsContainer;
import org.qiunet.utils.exceptions.CustomException;
import org.qiunet.utils.scanner.IApplicationContext;
import org.qiunet.utils.scanner.IApplicationContextAware;
import org.qiunet.utils.scanner.ScannerType;

import java.lang.reflect.Method;
import java.util.Map;

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
		this.handlerStatusTips(context);
		this.handlerResponse(context);
	}

	private void handlerStatusTips(IApplicationContext context) throws Exception{
		context.getSubTypesOf(IStatusTipsHandler.class).forEach(clz -> {
			if (! clz.isAssignableFrom(IBehaviorAction.class)) {
				throw new CustomException("game.test status tips [clz:{}] need be a IBehaviorAction!", clz.getName());
			}
			Method mtd = null;
			try {
				mtd = clz.getMethod("statusHandler", StatusTipsResponse.class);
			} catch (NoSuchMethodException e) {
				e.printStackTrace();
			}
			if (mtd == null) {
				return;
			}

			StatusTipsHandler handler = mtd.getAnnotation(StatusTipsHandler.class);
			if (handler == null) {
				return;
			}

			for (int status : handler.value()) {
				if (statusMapping.containsKey(status)) {
					throw new CustomException("game.test status tips [id:{}] is repeated!", status);
				}
				statusMapping.put(status, mtd);
			}
		});
	}

	private void handlerResponse(IApplicationContext context) {
		context.getMethodsAnnotatedWith(TestResponse.class).forEach(mtd -> {
			TestResponse annotation = mtd.getAnnotation(TestResponse.class);
			if (methodMapping.containsKey(annotation.value())) {
				throw new CustomException("game.test Response [id:{}] is repeated!", annotation.value());
			}

			if (! mtd.getDeclaringClass().isAssignableFrom(IBehaviorAction.class)) {
				throw new CustomException("Response [id:{}] need define in IBehaviorAction!", annotation.value());
			}

			if (mtd.getParameterCount() != 1) {
				throw new CustomException("Response [id:{}] just a IPbChannelData parameter.", annotation.value());
			}

			if (! mtd.getParameterTypes()[0].isAssignableFrom(IpbChannelData.class)) {
				throw new CustomException("Response [id:{}] parameter need implement IPbChannelData.", annotation.value());
			}
			methodMapping.put(annotation.value(), mtd);
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
