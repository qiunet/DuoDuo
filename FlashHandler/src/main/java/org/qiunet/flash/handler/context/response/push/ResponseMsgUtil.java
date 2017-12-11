package org.qiunet.flash.handler.context.response.push;

import io.netty.channel.Channel;
import io.netty.handler.codec.http.websocketx.BinaryWebSocketFrame;
import org.qiunet.flash.handler.context.session.ISession;
import org.qiunet.flash.handler.context.session.SessionManager;

/**
 *  消息推送
 * Created by qiunet.
 * 17/11/21
 */
public class ResponseMsgUtil {
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
		channel.writeAndFlush(message.encode());
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
		channel.writeAndFlush(new BinaryWebSocketFrame(message.encode().encodeToByteBuf()));
	}
}
