package org.qiunet.flash.handler.netty.server.param.adapter;

import org.qiunet.flash.handler.common.player.IMessageActor;
import org.qiunet.flash.handler.context.response.push.DefaultProtobufMessage;
import org.qiunet.flash.handler.context.session.DSession;
import org.qiunet.flash.handler.context.status.StatusResultException;
import org.qiunet.flash.handler.netty.server.param.adapter.message.HandlerNotFoundResponse;
import org.qiunet.flash.handler.netty.server.param.adapter.message.MessageTipsResponse;
import org.qiunet.flash.handler.netty.server.param.adapter.message.ServerCloseResponse;
import org.qiunet.flash.handler.netty.server.param.adapter.message.ServerExceptionResponse;
import org.qiunet.utils.async.LazyLoader;

/***
 *
 * 长连接情况下, 需要根据给定参数给出业务自己的session 和playerActor的实例对象
 *
 * @author qiunet
 * 2020/3/8 09:31
 **/
public interface IStartupContext<T extends IMessageActor<T>> {
	LazyLoader<DefaultProtobufMessage> HANDLER_NOT_FOUND_MESSAGE = new LazyLoader<>(() -> new HandlerNotFoundResponse().buildResponseMessage());
	LazyLoader<DefaultProtobufMessage> SERVER_EXCEPTION_MESSAGE = new LazyLoader<>(() -> new ServerExceptionResponse().buildResponseMessage());
	LazyLoader<DefaultProtobufMessage> SERVER_CLOSE_MESSAGE = new LazyLoader<>(() -> new ServerCloseResponse().buildResponseMessage());
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
	default DefaultProtobufMessage getHandlerNotFound() {
		return HANDLER_NOT_FOUND_MESSAGE.get();
	}

	/***
	 * 出现异常
	 * @param cause
	 * @return
	 */
	default DefaultProtobufMessage exception(Throwable cause){
		if (cause instanceof StatusResultException) {
			return MessageTipsResponse.valueOf(((StatusResultException) cause)).buildResponseMessage();
		}
		return SERVER_EXCEPTION_MESSAGE.get();
	}

	/**
	 * 服务没有开启
	 * @return
	 */
	default DefaultProtobufMessage serverClose() {
		return SERVER_CLOSE_MESSAGE.get();
	}
}
