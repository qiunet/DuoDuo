package org.qiunet.flash.handler.netty.server.config.adapter;

import io.netty.channel.ChannelFuture;
import org.qiunet.flash.handler.context.request.data.IChannelData;
import org.qiunet.flash.handler.context.session.ISession;
import org.qiunet.flash.handler.context.status.StatusResultException;
import org.qiunet.flash.handler.netty.server.config.adapter.message.ServerCloseRsp;
import org.qiunet.flash.handler.netty.server.config.adapter.message.ServerExceptionResponse;
import org.qiunet.flash.handler.netty.server.config.adapter.message.StatusTipsRsp;
import org.qiunet.utils.async.LazyLoader;
import org.qiunet.utils.logger.LoggerType;

/***
 *
 * 长连接情况下, 需要根据给定参数给出业务自己的session 和playerActor的实例对象
 *
 * @author qiunet
 * 2020/3/8 09:31
 **/
public interface IStartupContext {
	LazyLoader<IChannelData> SERVER_EXCEPTION_MESSAGE = new LazyLoader<>(ServerExceptionResponse::new);
	/***
	 * 出现异常
	 * @param cause
	 * @return
	 */
	default ChannelFuture exception(ISession session, Throwable cause){
		if (cause instanceof StatusResultException) {
			return session.sendMessage(StatusTipsRsp.valueOf(((StatusResultException) cause)), true);
		}
		LoggerType.DUODUO_FLASH_HANDLER.error("ChannelHandler异常", cause);
		return session.sendMessage(SERVER_EXCEPTION_MESSAGE.get(), true);
	}

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
