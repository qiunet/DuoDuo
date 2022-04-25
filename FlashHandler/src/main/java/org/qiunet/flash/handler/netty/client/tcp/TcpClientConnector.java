package org.qiunet.flash.handler.netty.client.tcp;

import org.qiunet.flash.handler.context.sender.IChannelMessageSender;
import org.qiunet.flash.handler.context.session.ISession;

/***
 * Tcp client 专门连接服务器的对象.
 * bootstrap 复用
 *
 * @author qiunet
 * 2020-11-06 12:25
 */
public class TcpClientConnector implements IChannelMessageSender {
	private final ISession session;

	TcpClientConnector(ISession session) {
		this.session = session;
	}

	@Override
	public ISession getSender() {
		return session;
	}

}
