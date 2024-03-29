package org.qiunet.flash.handler.netty.server.config.adapter;

import org.qiunet.flash.handler.context.session.ISession;
import org.qiunet.flash.handler.netty.server.config.adapter.message.ServerCloseRsp;

/***
 *
 * 长连接情况下, 需要根据给定参数给出业务自己的session 和playerActor的实例对象
 *
 * @author qiunet
 * 2020/3/8 09:31
 **/
public interface IStartupContext {
	/**
	 * 服务可用性校验
	 * 不可用. 触发 {@link ServerCloseRsp}
	 * @return true 可用 false 不可用
	 */
	default boolean userServerValidate(ISession session) { return true;}

	/**
	 * 玩家连接检查
	 * @param idKey 用户传入的id key
	 * @return
	 */
	default boolean userConnectionCheck(String idKey) { return true;}
}
