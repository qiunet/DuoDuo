package org.qiunet.flash.handler.netty.server.channel;

import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import org.qiunet.flash.handler.context.response.push.DefaultByteBufMessage;
import org.qiunet.flash.handler.context.response.push.IChannelMessage;
import org.qiunet.utils.logger.LoggerType;
import org.slf4j.Logger;

/**
 * 方便广播使用的组件
 * 使用示例:
 *
 * try(ChannelMessageBroadcast cmb = new ChannelMessageBroadcast(message)) {
 *     for(...) {
 *     		// 这个for也可以是walk. 反正把channel给出就ok
 *		    cmb.sendMessage(Channel);
 *     }
 * }
 *
 * Created by qiunet.
 * 17/11/30
 */
public class ChannelMessageBroadcast implements AutoCloseable {
	protected Logger logger = LoggerType.DUODUO_FLASH_HANDLER.getLogger();
	/**
	 * 协议id
	 */
	private final int protocolId;
	/**
	 * 广播内容
	 */
	private ByteBuf byteBuf;

	public ChannelMessageBroadcast(IChannelMessage message) {
		this.byteBuf = message.withoutHeaderByteBuf();
		this.protocolId = message.getProtocolID();
	}

	/**
	 * 用指定的channel 发送一个消息. 并且flush
	 * @param channel 发送到的channel
	 */
	public void sendMessage(Channel channel) {
		this.sendMessage(channel, true);
	}

	/**
	 * 发送一个消息
	 * @param channel channel
	 * @param flush 是否flush
	 */
	public void sendMessage(Channel channel, boolean flush) {
		channel.write(DefaultByteBufMessage.valueOf(protocolId, byteBuf.retain()));
		if (flush) {
			channel.flush();
		}
	}

	@Override
	protected void finalize() throws Throwable {
		if (byteBuf != null) {
			logger.error("not release protocolId [{}] ChannelMessageBroadcast", protocolId);
		}
		super.finalize();
	}

	@Override
	public void close() throws Exception {
		byteBuf.release();
		byteBuf = null;
	}
}
