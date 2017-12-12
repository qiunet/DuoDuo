package org.qiunet.flash.handler.context.session;

import io.netty.channel.ChannelHandlerContext;
import org.qiunet.flash.handler.common.enums.HandlerType;
import org.qiunet.flash.handler.context.request.IRequest;

/**
 * Created by qiunet.
 * 17/11/27
 */
public interface ISessionEvent {
	/***
	 * session 激活
	 * @param ctx
	 */
	void sessionRegistered(ChannelHandlerContext ctx);


	/***
	 * session 失活
	 * @param ctx
	 */
	void sessionUnregistered(ChannelHandlerContext ctx);

	/***
	 * 接收到消息
	 * @param ctx
	 * @param type
	 * @param msg
	 */
	void sessionReceived(ChannelHandlerContext ctx, HandlerType type, IRequest msg);
}
