package org.qiunet.flash.handler.context.sender;

import org.qiunet.flash.handler.context.request.data.pb.IpbChannelData;
import org.qiunet.flash.handler.context.response.push.IChannelMessage;
import org.qiunet.flash.handler.context.session.DSession;
import org.qiunet.flash.handler.context.session.future.IDSessionFuture;

/***
 * 所有发送响应的都实现该接口
 * @author qiunet
 * 2020/3/1 20:53
 **/
public interface IChannelMessageSender {
	/***
	 * 得到session
	 */
	DSession getSession();

	/**
	 * 发送消息
	 * @param message
	 * @return
	 */
	default IDSessionFuture sendMessage(IChannelMessage<?> message) {
		return getSession().sendMessage(message);
	}

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
	default IDSessionFuture sendMessage(IpbChannelData message, boolean flush) {
		return this.sendMessage(message.buildResponseMessage(), flush);
	}
	/**
	 * 发送消息
	 * @param message
	 */
	default IDSessionFuture sendMessage(IChannelMessage<?> message, boolean flush){
		return getSession().sendMessage(message, flush);
	}
}
