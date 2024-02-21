package org.qiunet.flash.handler.common.player.offline;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.qiunet.data.event.PlayerKickOutEvent;
import org.qiunet.flash.handler.common.player.offline.enums.OfflinePlayerDestroyCause;
import org.qiunet.flash.handler.common.player.server.UserServerState;
import org.qiunet.flash.handler.common.player.server.UserServerStateManager;
import org.qiunet.utils.listener.event.EventHandlerWeightType;
import org.qiunet.utils.listener.event.EventListener;
import org.qiunet.utils.listener.event.data.ServerClosedEvent;
import org.qiunet.utils.listener.event.data.ServerDeprecatedEvent;
import org.qiunet.utils.listener.event.data.ServerShutdownEvent;
import org.qiunet.utils.logger.LoggerType;

import java.util.List;
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
			Preconditions.checkState(userServerState != null && userServerState.getServerId() == 0, "PlayerId: %s serverId not empty!", playerId);
			OfflinePlayerActor actor = new OfflinePlayerActor(playerId);
			LoggerType.DUODUO_FLASH_HANDLER.info("Offline Player ID: {} created!", playerId);
			actor.scheduleMessage(a -> {
				this.remove(a.getPlayerId(), OfflinePlayerDestroyCause.TIMEOUT);
			}, 5, TimeUnit.MINUTES);
			return actor;
		});
	}

	@EventListener(EventHandlerWeightType.HIGH)
	private void serverShutdown(ServerShutdownEvent event) {
		this.removeAll(OfflinePlayerDestroyCause.SERVER_SHUTDOWN);
	}

	@EventListener
	private void serverDeprecate(ServerDeprecatedEvent event) {
		this.removeAll(OfflinePlayerDestroyCause.SERVER_DEPRECATE);
	}

	@EventListener
	private void serverClose(ServerClosedEvent event) {
		this.removeAll(OfflinePlayerDestroyCause.SERVER_CLOSE);
	}

	// 所有踢出事件最后执行. 免得事情UserOnline没有插入. 但是这里没有查到. 重新创建了
	@EventListener
	private void kickOut(PlayerKickOutEvent event) {
		this.remove(event.getPlayerId(), OfflinePlayerDestroyCause.KICK_OUT);
	}

	/**
	 * 移除所有
	 * @param cause 原因
	 */
	private void removeAll(OfflinePlayerDestroyCause cause) {
		List<Long> list = Lists.newArrayList(this.data.keySet());

		for (Long playerId : list) {
			this.remove(playerId, cause);
		}
	}
	/**
	 * 移除
	 * @param playerId 玩家id
	 */
	private void remove(long playerId, OfflinePlayerDestroyCause cause) {
		OfflinePlayerActor actor = this.data.remove(playerId);
		if (actor != null) {
			LoggerType.DUODUO_FLASH_HANDLER.info("Offline Player ID: {} destroy!", playerId);
			actor.destroy(cause);
		}
	}
}
