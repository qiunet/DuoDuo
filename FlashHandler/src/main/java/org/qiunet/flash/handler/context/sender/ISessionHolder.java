package org.qiunet.flash.handler.context.sender;

import io.netty.channel.ChannelFuture;
import org.qiunet.flash.handler.context.request.data.IChannelData;
import org.qiunet.flash.handler.context.response.push.IChannelMessage;
import org.qiunet.flash.handler.context.session.ISession;

/***
 * 所有发送响应的都实现该接口
 * @author qiunet
 * 2020/3/1 20:53
 **/
public interface ISessionHolder {
	/***
	 * 得到session
	 */
	ISession getSession();
	/**
	 * 发送消息
	 * @param message
	 * @return
	 */
	default ChannelFuture sendMessage(IChannelMessage<?> message) {
		return getSession().sendMessage(message);
	}

	/**
	 * 发送消息
	 * @param message
	 */
	default ChannelFuture sendMessage(IChannelData message) {
		return this.sendMessage(message.buildChannelMessage());
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
	 */
	default ChannelFuture sendMessage(IChannelMessage<?> message, boolean flush){
		return getSession().sendMessage(message, flush);
	}
}
