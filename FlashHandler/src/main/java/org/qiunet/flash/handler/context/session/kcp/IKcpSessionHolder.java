package org.qiunet.flash.handler.context.session.kcp;

import io.netty.channel.ChannelFuture;
import org.qiunet.flash.handler.context.request.data.IChannelData;
import org.qiunet.flash.handler.context.response.push.IChannelMessage;
import org.qiunet.flash.handler.context.session.ISession;

/***
 * 有kcp能力的Session
 * @author qiunet
 * 2023/3/24 21:04
 */
public interface IKcpSessionHolder {
	/**
	 * 获得kcp session
	 * @return
	 */
	ISession getKcpSession();
	/**
	 * 是否有准备
	 * @return
	 */
	default boolean isKcpSessionPrepare() {
		return getKcpSession() != null && getKcpSession().isActive();
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
		return getKcpSession().sendMessage(message.asKcpMsg(), flush);
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
}
