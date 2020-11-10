package org.qiunet.flash.handler.util;

import com.google.common.base.Preconditions;
import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.util.Attribute;
import io.netty.util.AttributeKey;
import org.qiunet.flash.handler.common.message.MessageContent;
import org.qiunet.flash.handler.context.header.IProtocolHeader;
import org.qiunet.flash.handler.context.session.DSession;
import org.qiunet.flash.handler.netty.bytebuf.PooledBytebufFactory;
import org.qiunet.flash.handler.netty.server.constants.CloseCause;
import org.qiunet.flash.handler.netty.server.constants.ServerConstants;
import org.qiunet.flash.handler.netty.server.param.adapter.IProtocolHeaderAdapter;
import org.qiunet.utils.logger.LoggerType;
import org.slf4j.Logger;

public final class ChannelUtil {
	private static final Logger logger = LoggerType.DUODUO_FLASH_HANDLER.getLogger();
	private static final AttributeKey<DSession> SESSION_KEY = AttributeKey.newInstance("SESSION_CHANNEL_KEY");
	private ChannelUtil(){}
	/***
	 * 得到channel保存的ProtocolHeader数据
	 * @param channel
	 * @return
	 */
	public static IProtocolHeaderAdapter getProtolHeaderAdapter(Channel channel) {
		return channel.attr(ServerConstants.PROTOCOL_HEADER_ADAPTER).get();
	}

	/***
	 * 将一个MessageContent 转为 有 Header 的 ByteBuf
	 * @param content
	 * @param channel
	 * @return
	 */
	public static ByteBuf messageContentToByteBuf(MessageContent content, Channel channel) {
		IProtocolHeaderAdapter adapter = getProtolHeaderAdapter(channel);
		IProtocolHeader header = adapter.newHeader(content);
		//必须先执行encodeBytes 函数, 内部可能会压缩,加密, 修改header.getLength().
		byte[] bytes = header.encodeBytes(content.bytes());

		ByteBuf byteBuf = PooledBytebufFactory.getInstance().alloc(header.getLength() + adapter.getHeaderLength());
		header.writeToByteBuf(byteBuf);
		byteBuf.writeBytes(bytes);
		return byteBuf;
	}

	/***
	 * 关联Session 和 channel
	 * @param val
	 * @return
	 */
	public static boolean bindSession(DSession val) {
		Preconditions.checkNotNull(val);
		Attribute<DSession> attr = val.channel().attr(SESSION_KEY);
		boolean result = attr.compareAndSet(null, val);
		if (! result) {
			logger.error("Session [{}] Duplicate", val);
			val.close(CloseCause.LOGIN_REPEATED);
		}
		return result;
	}

	/***
	 * 得到一个Session
	 * @param channel
	 * @return
	 */
	public static DSession getSession(Channel channel) {
		return channel.attr(SESSION_KEY).get();
	}
}
