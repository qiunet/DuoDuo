package org.qiunet.flash.handler.common.player.offline;

import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;
import org.qiunet.data.db.loader.event.PlayerKickOutEvent;
import org.qiunet.flash.handler.common.player.PlayerActor;
import org.qiunet.flash.handler.common.player.event.LoginSuccessEvent;
import org.qiunet.flash.handler.common.player.event.PlayerActorLogoutEvent;
import org.qiunet.flash.handler.common.player.server.UserServerState;
import org.qiunet.flash.handler.common.player.server.UserServerStateManager;
import org.qiunet.utils.listener.event.EventHandlerWeightType;
import org.qiunet.utils.listener.event.EventListener;
import org.qiunet.utils.listener.event.data.ServerClosedEvent;
import org.qiunet.utils.listener.event.data.ServerDeprecatedEvent;
import org.qiunet.utils.listener.event.data.ServerShutdownEvent;

import java.util.Map;
import java.util.concurrent.TimeUnit;

/***
 * OfflinePlayerActor 管理
 *
 * @author qiunet
 * 2021/11/19 11:57
 */
public enum UserOfflineManager {
	instance;
	/**
	 * 离线的玩家Actor
	 */
	private final Map<Long, OfflinePlayerActor> data = Maps.newConcurrentMap();

	/**
	 * 获取或者生成一个不在线的玩家actor
	 * @param playerId 玩家ID
	 * @return Object of OfflinePlayerActor
	 */
	public OfflinePlayerActor get(long playerId) {
		return data.computeIfAbsent(playerId, id -> {
			UserServerState userServerState = UserServerStateManager.instance.getUserServerState(playerId);
			Preconditions.checkState(userServerState != null && userServerState.getServerId() == 0);
			OfflinePlayerActor actor = new OfflinePlayerActor(playerId);
			actor.scheduleMessage(PlayerActor::destroy, 5, TimeUnit.MINUTES);
			return actor;
		});
	}

	@EventListener(EventHandlerWeightType.HIGH)
	private void serverShutdown(ServerShutdownEvent event) {
		this.data.values().forEach(OfflinePlayerActor::destroy);
	}

	@EventListener
	private void serverDeprecate(ServerDeprecatedEvent event) {
		this.data.values().forEach(OfflinePlayerActor::destroy);
	}

	@EventListener
	private void serverClose(ServerClosedEvent event) {
		this.data.values().forEach(OfflinePlayerActor::destroy);
	}

	// 所有踢出事件最后执行. 免得事情UserOnline没有插入. 但是这里没有查到. 重新创建了
	@EventListener
	private void kickOut(PlayerKickOutEvent event) {
		this.remove(event.getPlayerId());
	}
	@EventListener
	private void loginEvent(LoginSuccessEvent event) {
		this.remove(event.getPlayer().getId());
	}
	@EventListener
	private void logoutEvent(PlayerActorLogoutEvent event) {
		this.remove(event.getPlayer().getPlayerId());
	}

	/**
	 * 移除
	 * @param playerId
	 */
	void remove(long playerId) {
		OfflinePlayerActor actor = this.data.remove(playerId);
		if (actor != null) {
			actor.destroy();
		}
	}
}
