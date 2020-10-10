package org.qiunet.cross.node;

import org.qiunet.flash.handler.common.player.AbstractMessageActor;
import org.qiunet.flash.handler.common.player.IMessageActor;
import org.qiunet.flash.handler.context.request.data.pb.IpbChannelData;
import org.qiunet.flash.handler.context.session.DSession;
import org.qiunet.flash.handler.netty.client.param.TcpClientParams;
import org.qiunet.flash.handler.netty.client.tcp.NettyTcpClient;
import org.qiunet.flash.handler.netty.server.constants.ServerConstants;

import java.text.MessageFormat;

/***
 *
 *
 * @author qiunet
 * 2020-10-09 11:13
 */
public class ServerNode extends AbstractMessageActor<ServerNode> {

	private ServerInfo serverInfo;

	public ServerNode(DSession session) {
		super(session);
	}


	public static ServerNode valueOf(ServerInfo serverInfo) {
		NettyTcpClient tcpClient = new NettyTcpClient(TcpClientParams.custom()
			.setAddress(serverInfo.getHost(), serverInfo.getPort())
			.build(), ((session1, data) -> {
			IMessageActor messageActor = session1.getAttachObj(ServerConstants.MESSAGE_ACTOR_KEY);
		}));

		ServerNode node = new ServerNode(tcpClient.getSession());
		node.serverInfo = serverInfo;
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
	public void writeMessage(IpbChannelData message) {
		this.send(message.buildResponseMessage());
	}

	@Override
	protected String getIdent() {
		return MessageFormat.format( "ServerNode[{0}:{1}]", serverInfo.getHost(), serverInfo.getPort());
	}

	@Override
	public long getId() {
		if (serverInfo == null) return 0;
		return getServerId();
	}
}
