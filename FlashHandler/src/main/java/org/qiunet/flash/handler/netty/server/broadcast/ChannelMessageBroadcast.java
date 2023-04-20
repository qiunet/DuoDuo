package org.qiunet.flash.handler.netty.server.broadcast;

import io.netty.buffer.ByteBuf;
import org.qiunet.flash.handler.context.request.data.IChannelData;
import org.qiunet.flash.handler.context.response.push.DefaultByteBufMessage;
import org.qiunet.flash.handler.context.response.push.IChannelMessage;
import org.qiunet.flash.handler.context.session.IPlayerSender;
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

	public ChannelMessageBroadcast(IChannelData channelData) {
		this(channelData.buildChannelMessage());
	}

	public ChannelMessageBroadcast(IChannelMessage<?> message) {
		this.byteBuf = message.withoutHeaderByteBuf();
		this.protocolId = message.getProtocolID();
	}
	/**
	 * 发送一个消息
	 * @param sender session
	 * @param kcp 是否是kcp
	 * @param flush 是否flush
	 */
	public void sendMessage(IPlayerSender sender, boolean kcp, boolean flush) {
		DefaultByteBufMessage message = DefaultByteBufMessage.valueOf(protocolId, byteBuf.retain());
		if (kcp) {
			sender.sendKcpMessage(message, flush);
		}else {
			sender.sendMessage(message, flush);
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
