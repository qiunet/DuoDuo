package org.qiunet.game.test.response;

import org.qiunet.flash.handler.netty.server.param.adapter.message.StatusTipsRsp;
import org.qiunet.utils.args.ArgumentKey;

import java.lang.reflect.Method;
import java.util.Set;
import java.util.function.Function;

/***
 * 在 actionNode 处理状态
 * 处理的状态id
 * qiunet
 * 2021/8/8 23:01
 **/
public interface IStatusTipsHandler {
	/**
	 * 将status对应关系解析的逻辑提供者.
	 * status 使用注解的方式提供. 在逻辑里面. 自己解析.
	 **/
	ArgumentKey<Function<Method, Set<Integer>>> STATUS_MAPPING_HANDLER = new ArgumentKey<>();
	/**
	 * 处理状态.
	 *
	 * @param response
	 */
	default void statusHandler(StatusTipsRsp response) {}
}
