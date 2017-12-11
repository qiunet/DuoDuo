package org.qiunet.flash.handler.context.response.push;

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
		session.writeAndFlush(message.encode());
	}

	/***
	 * 推送一个Websocket message 给指定的客户端
	 * @param channelLongId
	 * @param message
	 */
	public static void responseWebsocketMessage(String channelLongId, IMessage message) {
		ISession session = SessionManager.getInstance().getSession(channelLongId);
		session.getChannel().writeAndFlush(message.encode().encodeToByteBuf());
	}
}
