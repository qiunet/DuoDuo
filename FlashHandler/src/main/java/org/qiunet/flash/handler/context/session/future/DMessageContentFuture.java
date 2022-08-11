package org.qiunet.flash.handler.context.session.future;

import io.netty.channel.Channel;
import io.netty.channel.DefaultChannelPromise;
import org.qiunet.flash.handler.context.response.push.IChannelMessage;

/***
 * 还没有连接成功时候, 发送的消息返回的就是该future
 *
 * qiunet
 * 2021/6/27 08:26
 **/
public class DMessageContentFuture extends DefaultChannelPromise {
	/**
	 * 携带的消息
	 */
	private final IChannelMessage<?> message;

	public DMessageContentFuture(Channel channel, IChannelMessage<?> message) {
		super(channel);
		this.message = message;
	}

	public IChannelMessage<?> getMessage() {
		return message;
	}
}
