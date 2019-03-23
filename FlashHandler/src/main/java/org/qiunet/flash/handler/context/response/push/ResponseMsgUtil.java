package org.qiunet.flash.handler.context.response.push;

import io.netty.channel.Channel;
import io.netty.handler.codec.http.websocketx.BinaryWebSocketFrame;
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
