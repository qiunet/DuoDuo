package org.qiunet.cross.node;

import io.netty.channel.ChannelFuture;
import org.qiunet.cross.event.CrossEventRequest;
import org.qiunet.flash.handler.common.IMessage;
import org.qiunet.flash.handler.common.player.AbstractMessageActor;
import org.qiunet.flash.handler.common.player.event.UserEvent;
import org.qiunet.flash.handler.context.request.data.IChannelData;
import org.qiunet.flash.handler.context.response.push.IChannelMessage;
import org.qiunet.flash.handler.context.session.ISession;
import org.qiunet.utils.listener.event.IListenerEvent;

import java.util.concurrent.CompletableFuture;

/***
 * 单独启动tcp连接, 提供其它服务公用的一个actor
 * 一个服务与一个服务之间只会有一个 连接.不会存在多个.
 *
 * @author qiunet
 * 2020-10-09 11:13
 */
public class ServerNode extends AbstractMessageActor<ServerNode> {
	/**
	 * server id
	 */
	private final int serverId;

	ServerNode(ISession session, int serverId) {
		super(session, String.valueOf(serverId));
		this.serverId = serverId;
	}


	@Override
	public CompletableFuture<Boolean> addMessage(IMessage<ServerNode> msg) {
		if (isAuth()) {
			// 是一个服务和另一个服务公用一个channel.
			// 由业务自己实现线程的安全. 一般CommMessageHandler  roomHandler等 重新addMessage 一遍.
			return this.runMessage(msg);
		}else {
			// 没有鉴权. 需要按照队列. 先执行鉴权操作.
			return super.addMessage(msg);
		}
	}
	/**
	 * 必须设置 serverId
	 *
	 * @param serverId
	 */
	@Override
	public void auth(long serverId) {
	}
	/**
	 * 服务与服务之间的事件触发 .走cross通道.
	 * @param eventData 事件
	 * @param <T>
	 */
	public <T extends IListenerEvent> void fireCrossEvent(T eventData) {
		CrossEventRequest request = CrossEventRequest.valueOf(eventData);
		this.sendMessage(request, true);
	}
	/**
	 * 服务给玩家的事件触发 .走cross通道.
	 * @param eventData 事件
	 * @param <T>
	 */
	public <T extends UserEvent> void fireUserCrossEvent(T eventData, long playerId) {
		CrossEventRequest request = CrossEventRequest.valueOf(eventData, playerId);
		this.sendMessage(request, true);
	}

	// 默认都flush
	@Override
	public ChannelFuture sendMessage(IChannelMessage<?> message) {
		return super.sendMessage(message, true);
	}

	@Override
	public ChannelFuture sendMessage(IChannelData message) {
		return super.sendMessage(message, true);
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

	@Override
	protected void exceptionHandle(Throwable e) {
		logger.error("Server Node exception:", e);
	}
}
