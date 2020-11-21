package org.qiunet.cross.node;

import io.netty.channel.ChannelFuture;
import org.qiunet.flash.handler.common.IMessage;
import org.qiunet.flash.handler.common.player.AbstractMessageActor;
import org.qiunet.flash.handler.context.request.data.pb.IpbChannelData;
import org.qiunet.flash.handler.context.session.DSession;
import org.qiunet.flash.handler.netty.server.constants.ServerConstants;

/***
 * 单独启动tcp连接, 提供其它服务公用的一个actor
 * 一个服务与一个服务之间只会有一个 连接.不会存在多个.
 *
 * @author qiunet
 * 2020-10-09 11:13
 */
public class ServerNode extends AbstractMessageActor<ServerNode> {
	private int serverId;

	public ServerNode(DSession session) {
		super(session.flushConfig(true, 0));
	}

	static ServerNode valueOf(DSession session, int serverId) {
		ServerNode serverNode = new ServerNode(session);
		serverNode.getSession().attachObj(ServerConstants.MESSAGE_ACTOR_KEY, serverNode);
		serverNode.serverId = serverId;
		return serverNode;

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
		this.writeMessage(ServerNodeAuthResponse.valueOf(ret));
	}

	/**
	 * 获得serverId
	 * @return
	 */
	public int getServerId() {
		return serverId;
	}

	/**
	 * 向服务器发起一个请求
	 * @param message
	 */
	public ChannelFuture writeMessage(IpbChannelData message) {
		return this.send(message.buildResponseMessage());
	}

	@Override
	public long getId() {
		return getServerId();
	}

}
