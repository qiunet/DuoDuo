package org.qiunet.flash.handler.util;

import com.google.common.base.Preconditions;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.util.Attribute;
import org.qiunet.flash.handler.common.message.MessageContent;
import org.qiunet.flash.handler.context.header.IProtocolHeader;
import org.qiunet.flash.handler.context.header.IProtocolHeaderType;
import org.qiunet.flash.handler.context.session.DSession;
import org.qiunet.flash.handler.netty.server.constants.CloseCause;
import org.qiunet.flash.handler.netty.server.constants.ServerConstants;
import org.qiunet.utils.logger.LoggerType;
import org.qiunet.utils.string.StringUtil;
import org.slf4j.Logger;

import java.net.InetSocketAddress;
import java.util.Arrays;

public final class ChannelUtil {
	private static final Logger logger = LoggerType.DUODUO_FLASH_HANDLER.getLogger();
	private ChannelUtil(){}
	/***
	 * 得到channel保存的ProtocolHeader数据
	 * @param channel
	 * @return
	 */
	public static IProtocolHeaderType getProtocolHeaderAdapter(Channel channel) {
		return channel.attr(ServerConstants.PROTOCOL_HEADER_ADAPTER).get();
	}

	/***
	 * 将一个MessageContent 写入byteBuf
	 * @param content
	 * @param channel
	 */
	public static void messageContentToByteBuf(MessageContent content, Channel channel, ByteBuf out) {
		IProtocolHeaderType adapter = getProtocolHeaderAdapter(channel);
		IProtocolHeader header = adapter.outHeader(content);
		out.writeBytes(header.dataBytes());
		out.writeBytes(content.bytes());
		if (LoggerType.DUODUO_FLASH_HANDLER.isDebugEnabled()) {
			LoggerType.DUODUO_FLASH_HANDLER.debug("header: {}", Arrays.toString(header.dataBytes()));
			LoggerType.DUODUO_FLASH_HANDLER.debug("body: {}", Arrays.toString(content.bytes()));
		}
	}
	/***
	 * 将一个MessageContent 转为 有 Header 的 ByteBuf
	 * @param content
	 * @param channel
	 * @return
	 */
	public static ByteBuf messageContentToByteBuf(MessageContent content, Channel channel) {
		IProtocolHeaderType adapter = getProtocolHeaderAdapter(channel);
		IProtocolHeader header = adapter.outHeader(content);
		//必须先执行encodeBytes 函数, 内部可能会压缩,加密, 修改header.getLength().
		byte[] headerBytes = header.dataBytes();
		return Unpooled.wrappedBuffer(headerBytes, content.bytes());
	}

	/***
	 * 关联Session 和 channel
	 * @param val
	 * @return
	 */
	public static boolean bindSession(DSession val) {
		Preconditions.checkNotNull(val);
		Attribute<DSession> attr = val.channel().attr(ServerConstants.SESSION_KEY);
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
		return channel.attr(ServerConstants.SESSION_KEY).get();
	}

	/**
	 *  获得ip
	 * @return
	 */
	public static String getIp(Channel channel) {
		return getIp(channel.attr(ServerConstants.HTTP_WS_HEADER_KEY).get(), channel);
	}

	/**
	 * 得到真实ip. http类型的父类
	 * @param headers
	 * @return
	 */
	public static String getIp(HttpHeaders headers, Channel channel) {
		String ip;
		if (headers != null) {
			if (!StringUtil.isEmpty(ip = headers.get("x-forwarded-for")) && !"unknown".equalsIgnoreCase(ip)) {
				return ip;
			}

			if (! StringUtil.isEmpty(ip = headers.get("HTTP_X_FORWARDED_FOR")) && ! "unknown".equalsIgnoreCase(ip)) {
				return ip;
			}

			if (!StringUtil.isEmpty(ip = headers.get("x-forwarded-for-pound")) &&! "unknown".equalsIgnoreCase(ip)) {
				return ip;
			}

			if (!StringUtil.isEmpty(ip = headers.get("Proxy-Client-IP") ) &&! "unknown".equalsIgnoreCase(ip)) {
				return ip;
			}

			if (!StringUtil.isEmpty(ip = headers.get("WL-Proxy-Client-IP")) &&! "unknown".equalsIgnoreCase(ip)) {
				return ip;
			}
		}
		return ((InetSocketAddress) channel.remoteAddress()).getAddress().getHostAddress();

	}
}
