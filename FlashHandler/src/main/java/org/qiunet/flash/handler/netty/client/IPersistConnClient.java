package org.qiunet.flash.handler.netty.client;

import org.qiunet.flash.handler.common.message.MessageContent;
import org.qiunet.flash.handler.context.request.data.pb.IpbChannelData;
import org.qiunet.flash.handler.context.response.push.IChannelMessage;
import org.qiunet.flash.handler.context.session.future.IDSessionFuture;

/**
 * 长连接客户端
 * Created by qiunet.
 * 17/12/8
 */
public interface IPersistConnClient {
	/***
	 * 发送消息
	 * @param content
	 */
	IDSessionFuture sendMessage(MessageContent content);

	/**
	 * 发送消息
	 * @param message
	 */
	default IDSessionFuture sendMessage(IChannelMessage message) {
		return this.sendMessage(message.encode());
	}
	/**
	 * 发送消息
	 * @param message
	 */
	default IDSessionFuture sendMessage(IpbChannelData message) {
		return this.sendMessage(message.buildResponseMessage());
	}
}
