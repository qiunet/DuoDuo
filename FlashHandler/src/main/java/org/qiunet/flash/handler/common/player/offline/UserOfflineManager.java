package org.qiunet.flash.handler.common.player.offline;

import com.google.common.base.Preconditions;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import org.qiunet.cross.node.ServerNodeManager;
import org.qiunet.data.event.PlayerKickOutEvent;
import org.qiunet.flash.handler.common.player.server.UserServerState;
import org.qiunet.flash.handler.common.player.server.UserServerStateManager;
import org.qiunet.utils.listener.event.EventHandlerWeightType;
import org.qiunet.utils.listener.event.EventListener;
import org.qiunet.utils.listener.event.data.ServerClosedEvent;
import org.qiunet.utils.listener.event.data.ServerDeprecatedEvent;
import org.qiunet.utils.listener.event.data.ServerShutdownEvent;
import org.qiunet.utils.logger.LoggerType;

import java.util.concurrent.ExecutionException;
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
	 * 已经shutdown
	 */
	private boolean shutdown;
	/**
	 * 离线的玩家Actor
	 */
	private final Cache<Long, OfflinePlayerActor> cache = CacheBuilder.newBuilder()
		.removalListener(obj -> {
			this.remove((OfflinePlayerActor) obj.getValue());
		})
		.expireAfterAccess(6, TimeUnit.MINUTES)
		.maximumSize(3000)
		.build();

	/**
	 * 获取或者生成一个不在线的玩家actor
	 * @param playerId 玩家ID
	 * @return Object of OfflinePlayerActor
	 */
	public OfflinePlayerActor get(long playerId) {
		OfflinePlayerActor playerActor = cache.getIfPresent(playerId);
		if (playerActor != null) {
			return playerActor;
		}

		if (shutdown) {
			return null;
		}

		UserServerState userServerState = UserServerStateManager.instance.getUserServerState(playerId);
		Preconditions.checkState(userServerState != null && userServerState.getServerId() == 0, "PlayerId: %s serverId not empty!", playerId);
		int serverId = UserServerStateManager.instance.tryLock(playerId, ServerNodeManager.getCurrServerId(), false);
		if (serverId != ServerNodeManager.getCurrServerId()) {
			return null;
		}

		try {
			return cache.get(playerId, () -> {
				OfflinePlayerActor actor = new OfflinePlayerActor(playerId);
				LoggerType.DUODUO_FLASH_HANDLER.info("Offline Player ID: {} created!", playerId);
				actor.scheduleMessage(a -> {
					cache.invalidate(a.getPlayerId());
				}, 5, TimeUnit.MINUTES);
				return actor;
			});
		} catch (ExecutionException e) {
			LoggerType.DUODUO_FLASH_HANDLER.error("Offline Player ID: {} failed!", playerId, e);
			return null;
		}
	}

	@EventListener(EventHandlerWeightType.HIGH)
	private void serverShutdown(ServerShutdownEvent event) {
		this.shutdown = true;
		this.removeAll();
	}

	@EventListener
	private void serverDeprecate(ServerDeprecatedEvent event) {
		this.removeAll();
	}

	@EventListener
	private void serverClose(ServerClosedEvent event) {
		this.removeAll();
	}

	// 所有踢出事件最后执行. 免得事情UserOnline没有插入. 但是这里没有查到. 重新创建了
	@EventListener
	private void kickOut(PlayerKickOutEvent event) {
		cache.invalidate(event.getPlayerId());
	}

	/**
	 * 移除所有
	 */
	private void removeAll() {
		cache.invalidateAll();
	}
	/**
	 * 移除
	 */
	private void remove(OfflinePlayerActor actor) {
		if (actor == null) {
			return;
		}

		LoggerType.DUODUO_FLASH_HANDLER.info("Offline Player ID: {} destroy!", actor.getId());
		if (! actor.inSelfThread()) {
			actor.addMessage(a -> ((OfflinePlayerActor) a).remove());
		}else {
			actor.remove();
		}
	}
}
