package org.qiunet.game.test.response;

import com.google.common.collect.Maps;
import org.qiunet.flash.handler.context.request.data.ChannelData;
import org.qiunet.flash.handler.context.request.data.IChannelData;
import org.qiunet.function.ai.node.IBehaviorAction;
import org.qiunet.utils.args.ArgsContainer;
import org.qiunet.utils.exceptions.CustomException;
import org.qiunet.utils.reflect.ReflectUtil;
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


	@Override
	public void setApplicationContext(IApplicationContext context, ArgsContainer argsContainer) throws Exception {
		this.handlerResponse(context);
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
				throw new CustomException("game.test Response [id:{}] is repeated!", channelData.ID());
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

	@Override
	public ScannerType scannerType() {
		return ScannerType.GAME_TEST_RESPONSE;
	}
}
