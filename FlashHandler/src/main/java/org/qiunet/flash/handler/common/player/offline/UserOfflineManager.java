package org.qiunet.flash.handler.common.player.offline;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import org.qiunet.flash.handler.common.player.event.OfflineUserExecuteEvent;
import org.qiunet.flash.handler.common.player.event.OfflineUserRequestEvent;
import org.qiunet.utils.exceptions.CustomException;
import org.qiunet.utils.listener.event.EventListener;

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
	 * 离线的玩家Actor
	 */
	private final Cache<Long, OfflinePlayerActor> data = CacheBuilder.newBuilder()
			.expireAfterAccess(30, TimeUnit.MINUTES)
			.removalListener(data -> {
				OfflinePlayerActor actor = (OfflinePlayerActor) data.getValue();
				if (actor != null) {
					actor.destroy();
				}
			}).build();


	/**
	 * 获取或者生成一个不在线的玩家actor
	 * @param playerId
	 * @return
	 */
	public OfflinePlayerActor get(long playerId) {
		return getOrCreate(playerId);
	}

	@EventListener
	private void requestEvent(OfflineUserRequestEvent event) {
		OfflinePlayerActor playerActor = getOrCreate(event.getPlayerId());
		playerActor.fireEvent(event.getEventData());
	}

	@EventListener
	private void executeEvent(OfflineUserExecuteEvent event) {
		OfflinePlayerActor playerActor = getOrCreate(event.getPlayerId());
		playerActor.addMessage(a -> event.run());
	}

	/**
	 * 获取. 没有就创建一个
	 * @param playerId
	 * @return
	 */
	OfflinePlayerActor getOrCreate(long playerId) {
		try {
			return data.get(playerId, () -> new OfflinePlayerActor(playerId));
		} catch (ExecutionException e) {
			throw new CustomException(e, "Get OfflinePlayerActor error");
		}
	}

	/**
	 * 移除
	 * @param playerId
	 */
	void remove(long playerId) {
		data.invalidate(playerId);
	}
}
