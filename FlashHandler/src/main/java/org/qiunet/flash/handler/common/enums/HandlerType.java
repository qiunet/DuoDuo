package org.qiunet.flash.handler.common.enums;

import io.netty.channel.Channel;
import org.qiunet.flash.handler.common.IMessage;
import org.qiunet.flash.handler.common.message.MessageContent;
import org.qiunet.flash.handler.common.player.IMessageActor;
import org.qiunet.flash.handler.handler.IHandler;
import org.qiunet.utils.exceptions.CustomException;

/**
 * handler的类型. 区分使用
 * @author qiunet
 *         Created on 17/3/7 17:22.
 */
public enum HandlerType {
	/**
	 * 包括http  https
	 */
	HTTP {
		@Override
		public IMessage createRequestContext(MessageContent content, Channel channel, IHandler handler, IMessageActor messageActor) {
			throw new CustomException("Not support for that! Please do it manual!");
		}
	},
	/**
	 * tcp
	 *  但是udp可以使用该类型的context和handler
	 */
	TCP {
		@Override
		public IMessage createRequestContext(MessageContent content, Channel channel, IHandler handler, IMessageActor messageActor) {
			return handler.getDataType().createTcpRequestContext(content, channel, handler, messageActor);
		}
	},
	/**
	 * webSocket
	 */
	WEB_SOCKET {
		@Override
		public IMessage createRequestContext(MessageContent content, Channel channel, IHandler handler, IMessageActor messageActor) {
			return handler.getDataType().createWebSocketRequestContext(content, channel, handler, messageActor);
		}
	};

	/**
	 * 根据handlerType 创建requestContext
	 * @return
	 */
	public abstract IMessage createRequestContext(MessageContent content, Channel channel, IHandler handler, IMessageActor messageActor);
}
