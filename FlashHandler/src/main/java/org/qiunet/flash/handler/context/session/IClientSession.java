package org.qiunet.flash.handler.context.session;

import io.netty.channel.ChannelFuture;
import org.qiunet.flash.handler.context.request.data.IChannelData;
import org.qiunet.flash.handler.context.response.push.IChannelMessage;
import org.qiunet.flash.handler.context.sender.ISessionHolder;
import org.qiunet.flash.handler.context.status.StatusResult;

import java.util.function.BiConsumer;

/***
 * 消息发送的接口
 * @author qiunet
 * 2023/3/30 15:49
 */
public interface IClientSession extends ISessionHolder {
	/**
	 * 发送消息
	 * @param message 发送消息
	 * @param consumer 回包消息消费 第一个参数 判断是否成功 第二个参数 response
	 */
	default ChannelFuture sendMessage(IChannelData message, BiConsumer<StatusResult, IChannelData> consumer) {
		return this.sendMessage(message, false, consumer);
	}
	/**
	 * 发送消息
	 * @param message 发送消息
	 * @param consumer 回包消息消费 第一个参数 判断是否成功 第二个参数 response
	 */
	default ChannelFuture sendMessage(IChannelData message, boolean flush, BiConsumer<StatusResult, IChannelData> consumer) {
		return this.sendMessage(message.buildChannelMessage(), flush, consumer);
	}
	/**
	 * 发送消息
	 * @param message 发送消息
	 * @param consumer 回包消息消费 第一个参数 判断是否成功 第二个参数 response
	 */
	default ChannelFuture sendMessage(IChannelMessage<?> message, BiConsumer<StatusResult, IChannelData> consumer) {
		return this.sendMessage(message, false, consumer);
	}

	@Override
	default ChannelFuture sendMessage(IChannelMessage<?> message, boolean flush) {
		return this.sendMessage(message, flush, null);
	}

	/**
	 * 发送消息
	 * @param message 发送消息
	 * @param consumer 回包消息消费 第一个参数 判断是否成功 第二个参数 response
	 */
	ChannelFuture sendMessage(IChannelMessage<?> message, boolean flush, BiConsumer<StatusResult, IChannelData> consumer);
}
