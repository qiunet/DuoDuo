package org.qiunet.flash.handler.context.session;

import io.netty.channel.ChannelFuture;
import org.qiunet.flash.handler.context.request.data.IChannelData;
import org.qiunet.flash.handler.context.response.push.IChannelMessage;

/***
 * 消息发送的接口
 * @author qiunet
 * 2023/3/30 15:49
 */
public interface ISender {
	/**
	 * 发送消息
	 * @param message
	 */
	default ChannelFuture sendMessage(IChannelData message) {
		return this.sendMessage(message, false);
	}
	/**
	 * 发送消息
	 * @param message
	 */
	default ChannelFuture sendMessage(IChannelData message, boolean flush) {
		return this.sendMessage(message.buildChannelMessage(), flush);
	}
	/**
	 * 发送消息
	 * @param message
	 * @return
	 */
	default ChannelFuture sendMessage(IChannelMessage<?> message) {
		return this.sendMessage(message, false);
	}
	/**
	 * 发送消息
	 * @param message
	 */
	ChannelFuture sendMessage(IChannelMessage<?> message, boolean flush);
}
