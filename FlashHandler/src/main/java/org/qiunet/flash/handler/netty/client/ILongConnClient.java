package org.qiunet.flash.handler.netty.client;

import org.qiunet.flash.handler.common.message.MessageContent;
import org.qiunet.flash.handler.context.request.data.pb.IpbChannelData;
import org.qiunet.flash.handler.context.response.push.IChannelMessage;

/**
 * 长连接客户端
 * Created by qiunet.
 * 17/12/8
 */
public interface ILongConnClient {
	/***
	 * 发送消息
	 * @param content
	 */
	void sendMessage(MessageContent content);

	/**
	 * 发送消息
	 * @param message
	 */
	default void sendMessage(IChannelMessage message) {
		this.sendMessage(message.encode());
	}
	/**
	 * 发送消息
	 * @param message
	 */
	default void sendMessage(IpbChannelData message) {
		this.sendMessage(message.buildResponseMessage());
	}
}
