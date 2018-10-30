package org.qiunet.flash.handler.context.session;

import io.netty.channel.Channel;
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
	 * @param channel
	 */
	void sessionRegistered(Channel channel);


	/***
	 * session 失活
	 * @param channel
	 */
	void sessionUnregistered(Channel channel);

	/***
	 * 接收到消息
	 * @param channel
	 * @param type
	 * @param msg
	 */
	void sessionReceived(Channel channel, HandlerType type, IRequest msg);
}
