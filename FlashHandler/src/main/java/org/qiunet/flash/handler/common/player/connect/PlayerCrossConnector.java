package org.qiunet.flash.handler.common.player.connect;

import io.netty.channel.ChannelFuture;
import io.netty.util.AttributeKey;
import org.qiunet.cross.actor.auth.CrossPlayerAuthRequest;
import org.qiunet.cross.event.CrossEventRequest;
import org.qiunet.cross.node.ServerNodeManager;
import org.qiunet.flash.handler.common.player.PlayerActor;
import org.qiunet.flash.handler.common.player.event.UserEvent;
import org.qiunet.flash.handler.context.sender.ISessionHolder;
import org.qiunet.flash.handler.context.session.ISession;
import org.qiunet.flash.handler.context.session.NodeClientSession;
import org.qiunet.flash.handler.context.session.NodeSessionType;
import org.qiunet.flash.handler.netty.server.constants.CloseCause;
import org.qiunet.flash.handler.netty.server.constants.ServerConstants;
import org.qiunet.utils.exceptions.CustomException;

import java.util.function.Consumer;

/***
 *  tcp 连接
 *
 * @author qiunet
 * 2021/11/16 17:32
 */
public class PlayerCrossConnector implements ISessionHolder {
	private static final AttributeKey<PlayerCrossConnector> CONNECTOR = AttributeKey.valueOf("player_cross_connector");

	/**
	 * session
	 */
	private NodeClientSession session;
	/**
	 * 玩家
	 */
	private final PlayerActor playerActor;
	/**
	 *
	 */
	private final long playerId;
	/**
	 * serverId
	 */
	private final int serverId;
	/**
	 * 使用actor 和 serverId 构造一个connector
	 * @param actor
	 * @return
	 */
	public PlayerCrossConnector(PlayerActor actor, int serverId) {
		if (serverId == ServerNodeManager.getCurrServerId()) {
			throw new CustomException("connect to self!");
		}
		this.playerId = actor.getPlayerId();
		this.serverId = serverId;
		this.playerActor = actor;
	}

	/**
	 * 从NodeClientSession 获取自身
	 * @param session
	 * @return
	 */
	public static PlayerCrossConnector get(NodeClientSession session) {
		return session.getAttachObj(CONNECTOR);
	}
	/**
	 * 连接
	 * @param callback 失败或者成功的回调
	 */
	public void connect(Consumer<Boolean> callback) {
		// 因为大部分服务都是内网组网. 所以使用host.如果以后不在内网. 有两个解决方案
		// 1. 直接修改下面为publicHost . 2. 云运营商跨区域组网
		this.session = new NodeClientSession(NodeSessionType.CROSS_PLAYER, CrossSessionManager.instance.getChannelPool(this.serverId), this.playerId);
		CrossPlayerAuthRequest request = CrossPlayerAuthRequest.valueOf(playerId, ServerNodeManager.getCurrServerId());
		CrossSessionManager.instance.addNewSession(playerId, this.serverId, this.session);
		this.session.attachObj(ServerConstants.MESSAGE_ACTOR_KEY, playerActor);
		ChannelFuture future = this.session.sendMessage(request, true);
		future.addListener(f -> callback.accept(f.isSuccess()));
		this.session.attachObj(CONNECTOR, this);
	}

	/**
	 * 退出跨服
	 * @param cause 原因
	 */
	public void quit(CloseCause cause) {
		this.getSession().close(cause);
	}

	public int getServerId() {
		return serverId;
	}
	/**
	 * 获得session
	 * @return
	 */
	@Override
	public ISession getSession() {
		return session;
	}
	/**
	 * 触发该服务的事件
	 * @param event
	 * @param <Event>
	 */
	public <Event extends UserEvent> void fireCrossEvent(Event event) {
		CrossEventRequest request = CrossEventRequest.valueOf(event);
		session.sendMessage(request.buildChannelMessage(), true);
	}
}
