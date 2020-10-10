package org.qiunet.cross.node;

import org.qiunet.flash.handler.context.response.push.IChannelMessage;
import org.qiunet.flash.handler.context.session.DSession;
import org.qiunet.flash.handler.netty.client.trigger.ILongConnResponseTrigger;

/***
 *
 *
 * @author qiunet
 * 2020-10-09 11:13
 */
public class ServerNode {

	private ServerInfo serverInfo;

	private DSession dSession;

	public static ServerNode valueOf(ServerInfo serverInfo, ILongConnResponseTrigger trigger) {
		ServerNode node = new ServerNode();
		node.serverInfo = serverInfo;
//		NettyTcpClient tcpClient = new NettyTcpClient(TcpClientParams.custom()
//			.setAddress(serverInfo.getHost(), serverInfo.getPort())
//			.setStartupContextAdapter()
//			.build(), trigger);
//		node.dSession = new DSession();
		return node;

	}
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
	public void writeMessage(IChannelMessage message) {
		dSession.writeMessage(message);
	}
}
