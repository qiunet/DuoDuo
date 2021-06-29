package org.qiunet.flash.handler.netty.client.tcp;

import org.qiunet.flash.handler.common.message.MessageContent;
import org.qiunet.flash.handler.context.session.DSession;
import org.qiunet.flash.handler.context.session.future.DChannelFutureWrapper;
import org.qiunet.flash.handler.context.session.future.IDSessionFuture;
import org.qiunet.flash.handler.netty.client.IPersistConnClient;

/***
 * Tcp client 专门连接服务器的对象.
 * bootstrap 复用
 *
 * @author qiunet
 * 2020-11-06 12:25
 */
public class TcpClientConnector implements IPersistConnClient {
	private final DSession session;

	TcpClientConnector(DSession session) {
		this.session = session;
	}

	@Override
	public DSession getSession() {
		return session;
	}

	@Override
	public IDSessionFuture sendMessage(MessageContent content) {
		return new DChannelFutureWrapper(session.channel().writeAndFlush(content));
	}
}
