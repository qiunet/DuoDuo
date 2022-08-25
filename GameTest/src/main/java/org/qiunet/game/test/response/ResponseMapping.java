package org.qiunet.game.test.response;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Maps;
import com.google.common.collect.SetMultimap;
import org.qiunet.flash.handler.context.request.data.ChannelData;
import org.qiunet.flash.handler.context.request.data.IChannelData;
import org.qiunet.flash.handler.netty.server.param.adapter.message.StatusTipsRsp;
import org.qiunet.function.ai.node.IBehaviorAction;
import org.qiunet.game.test.robot.action.BaseRobotAction;
import org.qiunet.utils.args.ArgsContainer;
import org.qiunet.utils.exceptions.CustomException;
import org.qiunet.utils.reflect.ReflectUtil;
import org.qiunet.utils.scanner.IApplicationContext;
import org.qiunet.utils.scanner.IApplicationContextAware;
import org.qiunet.utils.scanner.ScannerType;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.Set;

/***
 * 测试用例里面response 扫描
 *
 * @author qiunet
 * 2021-07-07 15:15
 */
public class ResponseMapping implements IApplicationContextAware {

	private static final Map<Integer, Method> methodMapping = Maps.newHashMap();

	private static final SetMultimap<Integer, Method> statusMapping = HashMultimap.create();
	@Override
	public void setApplicationContext(IApplicationContext context, ArgsContainer argsContainer) throws Exception {
		this.handlerResponse(context);
		this.handlerStatus(context);
	}

	private void handlerStatus(IApplicationContext context) {
		context.getMethodsAnnotatedWith(ResponseStatus.class).forEach(mtd -> {
			if (mtd.getParameterCount() != 1 || mtd.getParameterTypes()[0] != StatusTipsRsp.class) {
				throw new CustomException("ResponseStatus [{}#{}] need a StatusTipsRsp parameter.", mtd.getDeclaringClass().getName(), mtd.getName());
			}


			Class<?> declaringClass = mtd.getDeclaringClass();
			if (!BaseRobotAction.class.isAssignableFrom(declaringClass)) {
				throw new CustomException("ResponseStatus [{}#{}] need define in Robot Action!", mtd.getDeclaringClass().getName(), mtd.getName());
			}

			ResponseStatus annotation = mtd.getAnnotation(ResponseStatus.class);
			ReflectUtil.makeAccessible(mtd);
			for (int status : annotation.value()) {
				statusMapping.put(status, mtd);
			}
		});
	}


	private void handlerResponse(IApplicationContext context) {
		context.getMethodsAnnotatedWith(TestResponse.class).forEach(mtd -> {
			if (mtd.getParameterCount() != 1) {
				throw new CustomException("Response [{}#{}] just need a IChannelData parameter.", mtd.getDeclaringClass().getName(), mtd.getName());
			}

			if (! IChannelData.class.isAssignableFrom(mtd.getParameterTypes()[0])) {
				throw new CustomException("Response [{}#{}] parameter need implement IChannelData.", mtd.getDeclaringClass().getName(), mtd.getName());
			}

			ChannelData channelData = mtd.getParameterTypes()[0].getAnnotation(ChannelData.class);
			if (methodMapping.containsKey(channelData.ID())) {
				throw new CustomException("game.test Response [id:{}] is repeated! detail: {} {}", channelData.ID(),
						methodMapping.get(channelData.ID()).getDeclaringClass().getSimpleName(),
						mtd.getDeclaringClass().getSimpleName()
				);
			}

			if (! IBehaviorAction.class.isAssignableFrom(mtd.getDeclaringClass())) {
				throw new CustomException("Response [id:{}] need define in IBehaviorAction!", channelData.ID());
			}

			methodMapping.put(channelData.ID(), mtd);
			ReflectUtil.makeAccessible(mtd);
		});
	}
	/**
	 * 获得responseID对应的方法
	 * @param responseId
	 * @return
	 */
	public static Method getResponseMethodByID(int responseId) {
		return methodMapping.get(responseId);
	}

	/**
	 * 获得处理status的方法set
	 * @param status
	 * @return
	 */
	public static Set<Method> getResponseStatusHandler(int status) {
		return statusMapping.get(status);
	}

	@Override
	public ScannerType scannerType() {
		return ScannerType.GAME_TEST_RESPONSE;
	}
}
