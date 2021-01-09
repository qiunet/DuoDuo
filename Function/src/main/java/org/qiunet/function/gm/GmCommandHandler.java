package org.qiunet.function.gm;

import com.google.common.collect.ComparisonChain;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.qiunet.flash.handler.common.player.AbstractPlayerActor;
import org.qiunet.flash.handler.context.status.IGameStatus;
import org.qiunet.function.gm.message.resp.GmCommandInfo;
import org.qiunet.utils.args.ArgsContainer;
import org.qiunet.utils.exceptions.CustomException;
import org.qiunet.utils.scanner.IApplicationContext;
import org.qiunet.utils.scanner.IApplicationContextAware;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/***
 * 处理gm命令
 *
 * @author qiunet
 * 2021-01-08 12:05
 */
enum GmCommandHandler implements IApplicationContextAware {
	instance;
	/**
	 * 参数的定义
	 */
	private final Map<Integer, CommandHandlerMethodInfo> handlers = Maps.newHashMap();
	/**
	 * 给客户端的列表
	 */
	private List<GmCommandInfo> infoList;

	@Override
	public void setApplicationContext(IApplicationContext context, ArgsContainer argsContainer) throws Exception {
		Set<Method> methods = context.getMethodsAnnotatedWith(GmCommand.class);
		for (Method method : methods) {
			GmCommand annotation = method.getAnnotation(GmCommand.class);
			if (handlers.containsKey(annotation.type())) {
				throw new CustomException("Gm command handler type {} repeated!", annotation.type());
			}

			if (! IGameStatus.class.isAssignableFrom(method.getReturnType())) {
				throw new CustomException("method {}#{} must return IGameStatus type!", method.getDeclaringClass().getName(),method.getName());
			}

			handlers.put(annotation.type(), new CommandHandlerMethodInfo(method));
		}

		this.infoList = handlers.values().stream()
				.sorted((o1, o2) -> ComparisonChain.start()
						.compare(o2.annotation.order(), o1.annotation.order())
						.compare(o1.annotation.commandName(), o2.annotation.commandName())
						.result()
				).map(data -> GmCommandInfo.valueOf(data.annotation.type(), data.annotation.commandName(), data.paramList))
				.collect(Collectors.toList());
	}

	static class CommandHandlerMethodInfo {
		/**
		 * 参数
 		 */
		List<GmParam> paramList;
		/**
		 * 处理的方法
		 */
		Method method;
		/**
		 * 注解
		 */
		GmCommand annotation;

		public CommandHandlerMethodInfo(Method method) {
			this.method = method;
			this.annotation = method.getAnnotation(GmCommand.class);
			this.paramList = Lists.newArrayListWithCapacity(method.getParameterCount() - 1);
			if (! AbstractPlayerActor.class.isAssignableFrom(method.getParameters()[0].getType())) {
				throw new CustomException("Gm Command method the first parameter must be PlayerActor");
			}


			for (int i = 1; i < method.getParameters().length; i++) {
				Parameter parameter = method.getParameters()[i];
				this.paramList.add(GmParam.valueOf(GmParamType.parse(parameter.getType()), parameter.getName()));
			}
		}
	}
}