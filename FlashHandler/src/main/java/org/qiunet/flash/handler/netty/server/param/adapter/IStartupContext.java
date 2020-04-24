package org.qiunet.flash.handler.netty.server.param.adapter;

import io.netty.channel.Channel;
import org.qiunet.flash.handler.common.player.IPlayerActor;
import org.qiunet.flash.handler.context.response.push.IResponseMessage;
import org.qiunet.flash.handler.context.session.ISession;

/***
 *
 * 长连接情况下, 需要根据给定参数给出业务自己的session 和playerActor的实例对象
 *
 * @author qiunet
 * 2020/3/8 09:31
 **/
public interface IStartupContext<S extends ISession, T extends IPlayerActor<S, ? extends IPlayerActor>> {
	/**
	 * 构造session http情况不会调用.
	 * @param channel
	 * @return
	 */
	S buildSession(Channel channel);

	/**
	 * 构造PlayerActor
	 * http情况不会调用.
	 * @param session
	 * @return
	 */
	T buildPlayerActor(ISession session);
	/***
	 *  没有找到handler 404
	 * @return
	 */
	IResponseMessage getHandlerNotFound();

	/***
	 * 出现异常
	 * @param cause
	 * @return
	 */
	IResponseMessage exception(Throwable cause);
}