package org.qiunet.flash.handler.netty.server.param.adapter;

import org.qiunet.flash.handler.common.player.IMessageActor;
import org.qiunet.flash.handler.context.request.data.IChannelData;
import org.qiunet.flash.handler.context.response.push.IChannelMessage;
import org.qiunet.flash.handler.context.session.DSession;
import org.qiunet.flash.handler.context.status.StatusResultException;
import org.qiunet.flash.handler.netty.server.param.adapter.message.*;
import org.qiunet.utils.async.LazyLoader;
import org.qiunet.utils.logger.LoggerType;

/***
 *
 * 长连接情况下, 需要根据给定参数给出业务自己的session 和playerActor的实例对象
 *
 * @author qiunet
 * 2020/3/8 09:31
 **/
public interface IStartupContext<T extends IMessageActor<T>> {
	LazyLoader<IChannelMessage<IChannelData>> HANDLER_NOT_FOUND_MESSAGE = new LazyLoader<>(() -> new HandlerNotFoundResponse().buildResponseMessage());
	LazyLoader<IChannelMessage<IChannelData>> SERVER_EXCEPTION_MESSAGE = new LazyLoader<>(() -> new ServerExceptionResponse().buildResponseMessage());
	LazyLoader<IChannelMessage<IChannelData>> SERVER_CLOSE_MESSAGE = new LazyLoader<>(() -> new ServerCloseResponse().buildResponseMessage());
	LazyLoader<IChannelMessage<IChannelData>> SERVER_PONG_MESSAGE = new LazyLoader<>(() -> new ServerPongResponse().buildResponseMessage());
	/**
	 * 构造MessageActor
	 * http情况不会调用.
	 * @param session
	 * @return
	 */
	T buildMessageActor(DSession session);
	/***
	 *  没有找到handler 404
	 * @return
	 */
	default IChannelMessage<IChannelData> getHandlerNotFound() {
		return HANDLER_NOT_FOUND_MESSAGE.get();
	}

	/***
	 * 出现异常
	 * @param cause
	 * @return
	 */
	default IChannelMessage<IChannelData> exception(Throwable cause){
		if (cause instanceof StatusResultException) {
			return StatusTipsResponse.valueOf(((StatusResultException) cause)).buildResponseMessage();
		}else {
			LoggerType.DUODUO_FLASH_HANDLER.error("异常", cause);
		}
		return SERVER_EXCEPTION_MESSAGE.get();
	}

	/**
	 * 服务没有开启
	 * @return
	 */
	default IChannelMessage<IChannelData> serverClose() {
		return SERVER_CLOSE_MESSAGE.get();
	}

	default IChannelMessage<IChannelData> serverPongMsg() {
		return SERVER_CLOSE_MESSAGE.get();
	}
}
