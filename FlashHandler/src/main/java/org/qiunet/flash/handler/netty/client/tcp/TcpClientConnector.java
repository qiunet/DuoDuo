package org.qiunet.flash.handler.netty.client.tcp;

import org.qiunet.flash.handler.context.sender.IChannelMessageSender;
import org.qiunet.flash.handler.context.session.DSession;

/***
 * Tcp client 专门连接服务器的对象.
 * bootstrap 复用
 *
 * @author qiunet
 * 2020-11-06 12:25
 */
public class TcpClientConnector implements IChannelMessageSender {
	private final DSession session;

	TcpClientConnector(DSession session) {
		this.session = session;
	}

	@Override
	public DSession getSession() {
		return session;
	}

}
