package org.qiunet.flash.handler.common.enums;

import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.handler.codec.http.websocketx.BinaryWebSocketFrame;
import org.qiunet.flash.handler.acceptor.ProcessAcceptor;
import org.qiunet.flash.handler.context.request.IRequestContext;
import org.qiunet.flash.handler.context.response.push.IMessage;

/**
 * handler的类型. 区分使用
 * @author qiunet
 *         Created on 17/3/7 17:22.
 */
public enum HandlerType {
	/**
	 * 包括http  https
	 */
	HTTP{
		@Override
		public void processRequest(IRequestContext context) {
			context.handler();
		}
	},
	/**
	 * tcp
	 *  但是udp可以使用该类型的context和handler
	 */
	TCP{
		@Override
		public void processRequest(IRequestContext context) {
			ProcessAcceptor.getInstance().process(context);
		}

		@Override
		public ChannelFuture writeAndFlush(Channel channel, IMessage message) {
			return channel.writeAndFlush(message.encode());
		}
	},
	/**
	 * webSocket
	 */
	WEB_SOCKET{
		@Override
		public void processRequest(IRequestContext context) {
			ProcessAcceptor.getInstance().process(context);
		}

		@Override
		public ChannelFuture writeAndFlush(Channel channel, IMessage message) {
			return channel.writeAndFlush(new BinaryWebSocketFrame(message.encode().encodeToByteBuf()));
		}
	},
	;

	/***
	 * 处理请求
	 * @param context
	 */
	public abstract void processRequest(IRequestContext context);
	/***
	 * 推送消息
	 * @param channel
	 * @param message
	 * @return
	 */
	public ChannelFuture writeAndFlush(Channel channel, IMessage message){
		return null;
	}
}
