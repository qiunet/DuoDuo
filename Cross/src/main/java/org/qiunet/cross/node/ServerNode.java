package org.qiunet.cross.node;

import org.qiunet.cross.common.trigger.TcpNodeClientTrigger;
import org.qiunet.cross.event.CrossEventManager;
import org.qiunet.flash.handler.common.IMessage;
import org.qiunet.flash.handler.common.player.AbstractMessageActor;
import org.qiunet.flash.handler.context.header.ProtocolHeaderType;
import org.qiunet.flash.handler.context.session.DSession;
import org.qiunet.flash.handler.netty.client.param.TcpClientParams;
import org.qiunet.flash.handler.netty.client.tcp.NettyTcpClient;
import org.qiunet.flash.handler.netty.server.constants.ServerConstants;
import org.qiunet.utils.listener.event.IEventData;

/***
 * 单独启动tcp连接, 提供其它服务公用的一个actor
 * 一个服务与一个服务之间只会有一个 连接.不会存在多个.
 *
 * @author qiunet
 * 2020-10-09 11:13
 */
public class ServerNode extends AbstractMessageActor<ServerNode> {
	private static final NettyTcpClient tcpClient = NettyTcpClient.create(TcpClientParams.custom().setProtocolHeaderType(ProtocolHeaderType.node).build(), new TcpNodeClientTrigger());

	private int serverId;

	public ServerNode(DSession session) {
		super(session);
	}

	ServerNode(ServerInfo serverInfo) {
		DSession session = tcpClient.connect(serverInfo.getHost(), serverInfo.getCommunicationPort(),
				f -> f.channel().attr(ServerConstants.MESSAGE_ACTOR_KEY).set(this));

		this.serverId = serverInfo.getServerId();
		super.setSession(session);

		// 发送鉴权请求
		this.sendMessage(ServerNodeAuthRequest.valueOf(ServerNodeManager.getCurrServerId()));
	}
	@Override
	public void addMessage(IMessage<ServerNode> msg) {
		if (isAuth()) {
			// 是一个服务和另一个服务公用一个channel.
			// 由业务自己实现线程的安全. 一般CommMessageHandler  roomHandler等 重新addMessage 一遍.
			this.runMessage(msg);
		}else {
			// 没有鉴权. 需要按照队列. 先执行鉴权操作.
			super.addMessage(msg);
		}
	}

	/**
	 * 必须设置 serverId
	 *
	 * @param serverId
	 */
	@Override
	public void auth(long serverId) {
		this.serverId = (int)serverId;
		boolean ret = ServerNodeManager0.instance.addNode(this);
	}
	/**
	 * 服务与服务之间的事件触发 .走cross通道.
	 * @param serverId 对方serverId
	 * @param eventData 事件
	 * @param <T>
	 */
	public static <T extends IEventData> void fireCrossEvent(int serverId, T eventData) {
		CrossEventManager.fireCrossEvent(serverId, eventData);
	}
	/**
	 * 获得serverId
	 * @return
	 */
	public int getServerId() {
		return serverId;
	}


	@Override
	public long getId() {
		return getServerId();
	}

}
