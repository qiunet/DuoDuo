package org.qiunet.flash.handler.context.response.push;

import io.netty.channel.Channel;
import io.netty.handler.codec.http.websocketx.BinaryWebSocketFrame;
import org.qiunet.flash.handler.context.session.ISession;
import org.qiunet.flash.handler.context.session.SessionManager;
import org.qiunet.flash.handler.netty.server.udp.handler.UdpChannel;
import org.qiunet.utils.logger.LoggerType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *  消息推送
 * Created by qiunet.
 * 17/11/21
 */
public class ResponseMsgUtil {
	private static final Logger logger = LoggerFactory.getLogger(LoggerType.DUODUO);
	/***
	 * 推送一个普通socket message 给指定的客户端
	 * @param channelLongId
	 * @param message
	 */
	public static void responseTcpMessage(String channelLongId, IMessage message) {
		ISession session = SessionManager.getInstance().getSession(channelLongId);
		responseTcpMessage(session.getChannel(), message);
	}

	/***
	 * 推送一个普通socket message 给指定的客户端
	 * @param channel
	 * @param message
	 */
	public static void responseTcpMessage(Channel channel, IMessage message) {
		if (logger.isInfoEnabled()) {
			logger.info(message.toStr());
		}
		channel.writeAndFlush(message.encode());
	}
	/***
	 * 推送一个普通udp message 给指定的客户端
	 * @param channel
	 * @param importantMsg 是否是需要保证可靠性的消息. true 需要 false 不需要
	 * @param message
	 */
	public static void responseUdpMessage(UdpChannel channel, boolean importantMsg, IMessage message) {
		if (logger.isInfoEnabled()) {
			logger.info(message.toStr());
		}
		channel.sendMessage(message.encode().encodeToByteBuf(), importantMsg);
	}

	/***
	 * 推送一个Websocket message 给指定的客户端
	 * @param channelLongId
	 * @param message
	 */
	public static void responseWebsocketMessage(String channelLongId, IMessage message) {
		ISession session = SessionManager.getInstance().getSession(channelLongId);
		responseWebsocketMessage(session.getChannel(), message);
	}
	/***
	 * 推送一个Websocket message 给指定的客户端
	 * @param channel
	 * @param message
	 */
	public static void responseWebsocketMessage(Channel channel, IMessage message) {
		if (logger.isInfoEnabled()) {
			logger.info(message.toStr());
		}
		channel.writeAndFlush(new BinaryWebSocketFrame(message.encode().encodeToByteBuf()));
	}
}
