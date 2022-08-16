package org.qiunet.flash.handler.context.sender;

import io.netty.channel.ChannelFuture;
import org.qiunet.flash.handler.context.request.data.IChannelData;
import org.qiunet.flash.handler.context.response.push.IChannelMessage;

/***
 * 所有发送响应的都实现该接口
 * @author qiunet
 * 2020/3/1 20:53
 **/
public interface IChannelMessageSender {
	/***
	 * 得到session
	 */
	IChannelMessageSender getSender();
	/**
	 * 是否有kcp session
	 * @return
	 */
	default boolean isKcpSessionPrepare() {
		return getSender().isKcpSessionPrepare();
	}

	/**
	 * 如果有绑定udp session.
	 * 发送udp消息
	 *
	 * @param message
	 * @return
	 */
	default ChannelFuture sendKcpMessage(IChannelMessage<?> message) {
		return this.sendKcpMessage(message, false);
	}
	/**
	 * 如果有绑定udp session.
	 * 发送udp消息
	 *
	 * @param message
	 * @return
	 */
	default ChannelFuture sendKcpMessage(IChannelMessage<?> message, boolean flush) {
		return getSender().sendKcpMessage(message, flush);
	}

	/**
	 * 如果有绑定udp session.
	 * 发送udp消息
	 *
	 * @param message
	 * @return
	 */
	default ChannelFuture sendKcpMessage(IChannelData message) {
		return this.sendKcpMessage(message, false);
	}
	/**
	 * 如果有绑定udp session.
	 * 发送udp消息
	 *
	 * @param message
	 * @return
	 */
	default ChannelFuture sendKcpMessage(IChannelData message, boolean flush) {
		return this.sendKcpMessage(message.buildChannelMessage(), flush);
	}
	/**
	 * 发送消息
	 * @param message
	 * @return
	 */
	default ChannelFuture sendMessage(IChannelMessage<?> message) {
		return getSender().sendMessage(message);
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
		return getSender().sendMessage(message, flush);
	}

	default void flush(){
		getSender().flush();
	}
}
