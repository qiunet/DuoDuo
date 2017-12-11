package org.qiunet.flash.handler.context.response.push;

import org.qiunet.flash.handler.context.session.ISession;
import org.qiunet.flash.handler.context.session.SessionManager;

/**
 * Created by qiunet.
 * 17/11/21
 */
public class ResponseMsgUtil {

	private volatile static ResponseMsgUtil instance;

	private ResponseMsgUtil() {
		if (instance != null) throw new RuntimeException("Instance Duplication!");
		instance = this;
	}

	public static ResponseMsgUtil getInstance() {
		if (instance == null) {
			synchronized (ResponseMsgUtil.class) {
				if (instance == null)
				{
					new ResponseMsgUtil();
				}
			}
		}
		return instance;
	}

	/***
	 * 推送一个message 给指定的客户端
	 * @param channelLongId
	 * @param message
	 */
	public void responseMessage(String channelLongId, IMessage message) {
		ISession session = SessionManager.getInstance().getSession(channelLongId);
		session.writeAndFlush(message.encode());
	}
}
