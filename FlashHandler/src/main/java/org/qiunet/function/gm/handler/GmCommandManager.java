package org.qiunet.function.gm.handler;

import com.google.common.collect.ComparisonChain;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.qiunet.flash.handler.common.player.PlayerActor;
import org.qiunet.flash.handler.context.status.IGameStatus;
import org.qiunet.function.gm.GmCommand;
import org.qiunet.function.gm.GmParam;
import org.qiunet.function.gm.GmParamDesc;
import org.qiunet.function.gm.GmParamType;
import org.qiunet.function.gm.proto.rsp.GmCommandInfo;
import org.qiunet.utils.args.ArgsContainer;
import org.qiunet.utils.exceptions.CustomException;
import org.qiunet.utils.logger.LoggerType;
import org.qiunet.utils.reflect.ReflectUtil;
import org.qiunet.utils.scanner.IApplicationContext;
import org.qiunet.utils.scanner.IApplicationContextAware;
import org.qiunet.utils.scanner.ScannerType;
import org.qiunet.utils.string.StringUtil;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

/***
 * 处理gm命令
 *
 * @author qiunet
 * 2021-01-08 12:05
 */
enum GmCommandManager implements IApplicationContextAware {
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
		List<Method> collect = context.getMethodsAnnotatedWith(GmCommand.class).stream().sorted(
				(o1, o2) -> ComparisonChain.start()
						.compare(o2.getAnnotation(GmCommand.class).order(), o1.getAnnotation(GmCommand.class).order())
						.compare(o1.getAnnotation(GmCommand.class).order(), o2.getAnnotation(GmCommand.class).order())
						.result()
		).collect(Collectors.toList());

		// 使用自己的排序. 不用外部给定type
		AtomicInteger id = new AtomicInteger();
		for (Method method : collect) {
			if (! IGameStatus.class.isAssignableFrom(method.getReturnType())) {
				throw new CustomException("method {}#{} must return IGameStatus type!", method.getDeclaringClass().getName(),method.getName());
			}

			handlers.computeIfAbsent(id.incrementAndGet(), key -> new CommandHandlerMethodInfo(key, context.getInstanceOfClass(method.getDeclaringClass()), method));
		}

		this.infoList = handlers.values().stream()
				.sorted((o1, o2) -> ComparisonChain.start()
						.compare(o2.annotation.order(), o1.annotation.order())
						.compare(o1.annotation.commandName(), o2.annotation.commandName())
						.result()
				).map(data -> GmCommandInfo.valueOf(data.type, data.annotation.commandName(), data.paramList))
				.collect(Collectors.toList());
	}

	@Override
	public ScannerType scannerType() {
		return ScannerType.GM_COMMAND;
	}

	/**
	 * 获得对应的 CommandHandlerMethodInfo
	 * @param type
	 * @return
	 */
	CommandHandlerMethodInfo getCommandInfo(int type) {
		return handlers.get(type);
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
		 * 所属对象实例
		 */
		private final Object obj;
		/*
	     * 分配的类型
		 */
		private final int type;
		/**
		 * 注解
		 */
		GmCommand annotation;

		public CommandHandlerMethodInfo(int type, Object obj, Method method) {
			this.paramList = Lists.newArrayListWithCapacity(method.getParameterCount() - 1);
			if (! PlayerActor.class.isAssignableFrom(method.getParameters()[0].getType())) {
				throw new CustomException("Gm Command method {}#{} the first parameter must be PlayerActor", method.getDeclaringClass().getName(), method.getName());
			}
			this.annotation = method.getAnnotation(GmCommand.class);
			ReflectUtil.makeAccessible(method);
			this.method = method;
			this.type = type;
			this.obj = obj;


			for (int i = 1; i < method.getParameters().length; i++) {
				Parameter parameter = method.getParameters()[i];
				GmParamDesc paramDesc = parameter.getAnnotation(GmParamDesc.class);

				String regex = paramDesc == null ? "" : paramDesc.regex();
				String example = paramDesc == null ? "" : paramDesc.example();
				String name = paramDesc == null ? parameter.getName() : paramDesc.value();

				if (!StringUtil.isEmpty(regex) && StringUtil.isEmpty(example)) {
					throw new CustomException("When regex not empty, example must specify value!");
				}

				this.paramList.add(GmParam.valueOf(GmParamType.parse(parameter.getType()), name, regex, example));
			}
		}

		/**
		 * 处理gm 命令
		 * @param player
		 * @param param
		 * @return
		 */
		public IGameStatus handler(PlayerActor player, List<String> param) {
			List<Object> params = Lists.newArrayListWithCapacity(param.size() + 1);
			params.add(player);
			for (int i = 0; i < paramList.size(); i++) {
				GmParam gmParam = paramList.get(i);
				String reqParam = param.get(i);

				params.add(gmParam.getType().parse(reqParam));
			}
			try {
				return (IGameStatus) method.invoke(obj, params.toArray());
			} catch (IllegalAccessException e) {
				LoggerType.DUODUO_FLASH_HANDLER.error("GM command handler error:", e);
			} catch (InvocationTargetException e) {
				LoggerType.DUODUO_FLASH_HANDLER.error("GM command handler error:", e.getTargetException());
			}
			return IGameStatus.FAIL;
		}
	}

	public List<GmCommandInfo> getInfoList() {
		return infoList;
	}
}
