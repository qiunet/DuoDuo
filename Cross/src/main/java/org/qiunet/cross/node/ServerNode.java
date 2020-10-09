package org.qiunet.cross.node;

import org.qiunet.flash.handler.context.response.push.IResponseMessage;
import org.qiunet.flash.handler.context.session.DSession;

/***
 *
 *
 * @author qiunet
 * 2020-10-09 11:13
 */
public class ServerNode {

	private ServerInfo serverInfo;

	private DSession dSession;

	/**
	 * 获得serverId
	 * @return
	 */
	public int getServerId() {
		return serverInfo.getServerId();
	}

	/**
	 * 向服务器发起一个请求
	 * @param message
	 */
	public void writeMessage(IResponseMessage message) {
		dSession.writeMessage(message);
	}
}
