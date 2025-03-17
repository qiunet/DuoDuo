package org.qiunet.flash.handler.util;

import com.google.common.base.Preconditions;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.util.Attribute;
import org.qiunet.flash.handler.context.header.IProtocolHeader;
import org.qiunet.flash.handler.context.session.HttpSession;
import org.qiunet.flash.handler.context.session.ISession;
import org.qiunet.flash.handler.netty.server.constants.CloseCause;
import org.qiunet.flash.handler.netty.server.constants.ServerConstants;
import org.qiunet.utils.logger.LoggerType;
import org.qiunet.utils.string.StringUtil;
import org.slf4j.Logger;

import static io.netty.handler.codec.http.HttpVersion.HTTP_1_1;

public final class ChannelUtil {
	private static final Logger logger = LoggerType.DUODUO_FLASH_HANDLER.getLogger();
	private ChannelUtil(){}
	/**
	 * 关闭通道 打印日志
	 * @param channel 通道
	 */
	public static void closeChannel(Channel channel , CloseCause cause, String errMessage, Object ... params) {
		ISession session = getSession(channel);
		errMessage = StringUtil.slf4jFormat(errMessage, params);
		if (session == null) {
			logger.error(StringUtil.slf4jFormat("Channel [{}] cause [{}] message: {}", channel.id().asShortText(), cause, errMessage));
			channel.close();
			return;
		}

		logger.error(StringUtil.slf4jFormat("session[{}] cause [{}] message:{}", session, cause, errMessage));
		session.close(cause);
	}
	/***
	 * 得到channel保存的ProtocolHeader数据
	 * @param channel
	 * @return
	 */
	public static IProtocolHeader getProtocolHeader(Channel channel) {
		return channel.attr(ServerConstants.PROTOCOL_HEADER).get();
	}
	/***
	 * 关联Session 和 channel
	 * @param val session
	 */
	public static void bindSession(ISession val, Channel channel) {
		bindSession(null, val, channel);
	}
	public static void bindSession(ISession oldVal, ISession val, Channel channel) {
		Preconditions.checkNotNull(val);
		Attribute<ISession> attr = channel.attr(ServerConstants.SESSION_KEY);
		boolean result = attr.compareAndSet(oldVal, val);
		if (! result) {
			logger.error("Session [{}] Duplicate", val);
			val.close(CloseCause.LOGIN_REPEATED);
		}
	}

	/**
	 * 是否是首次连接
	 * @param channel channel
	 * @return true 是. false 不是
	 */
	public static boolean isConnectReq(Channel channel) {
		if (channel == null) {
			return false;
		}

		Attribute<Boolean> attr = channel.attr(ServerConstants.ALREADY_CONNECT_KEY);
		return attr.setIfAbsent(true) == null;
	}

	/***
	 * 得到一个Session
	 * @param channel
	 * @return
	 */
	public static ISession getSession(Channel channel) {
		return channel.attr(ServerConstants.SESSION_KEY).get();
	}


	public static void sendHttpResponseStatusAndClose(HttpSession session, HttpResponseStatus status) {
		FullHttpResponse response = new DefaultFullHttpResponse(HTTP_1_1, status);
		session.channel().writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);
		session.channel().close();
	}

	public static void sendHttpResponseStatusAndClose(ChannelHandlerContext ctx, HttpResponseStatus status) {
		sendHttpResponseStatusAndClose((HttpSession) ChannelUtil.getSession(ctx.channel()), status);
	}
}
