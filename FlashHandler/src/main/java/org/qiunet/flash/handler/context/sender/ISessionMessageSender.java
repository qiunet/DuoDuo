package org.qiunet.flash.handler.context.sender;

import org.qiunet.flash.handler.context.request.data.pb.IpbChannelData;
import org.qiunet.flash.handler.context.response.push.IChannelMessage;
import org.qiunet.flash.handler.context.session.future.IDSessionFuture;

/***
 * session 的发送消息接口
 *
 * qiunet
 * 2021/6/28 07:42
 **/
public interface ISessionMessageSender {

	/**
	 * 发送消息
	 * @param message
	 */
	IDSessionFuture sendMessage(IChannelMessage<?> message);
	/**
	 * 发送消息
	 * @param message
	 */
	default IDSessionFuture sendMessage(IpbChannelData message) {
		return this.sendMessage(message.buildResponseMessage());
	}
	/**
	 * 发送消息
	 * @param message
	 */
	IDSessionFuture sendMessage(IChannelMessage<?> message, boolean flush);
	/**
	 * 发送消息
	 * @param message
	 */
	default IDSessionFuture sendMessage(IpbChannelData message, boolean flush) {
		return this.sendMessage(message.buildResponseMessage(), flush);
	}
}
