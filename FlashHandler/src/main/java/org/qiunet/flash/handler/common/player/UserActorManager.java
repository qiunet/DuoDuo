package org.qiunet.flash.handler.common.player;

import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;
import org.qiunet.flash.handler.common.player.event.AuthEventData;
import org.qiunet.listener.event.EventListener;

import java.util.Map;

/***
 * 用户的actor管理.
 * 包括功能服 和 跨服的.
 * 在功能服. 管理的是用户的真实连接.
 * 在跨服. 管理的是功能服代理的连接
 *
 * @author qiunet
 * 2020-10-16 10:25
 */
public class UserActorManager {
	private static final UserActorManager instance = new UserActorManager();
	private UserActorManager(){}

	private static Map<Long, AbstractUserActor> datas = Maps.newConcurrentMap();

	@EventListener
	private void addPlayerActor(AuthEventData eventData) {
		Preconditions.checkState(eventData.getUserActor().isAuth());

		datas.put(eventData.getUserActor().getId(), eventData.getUserActor());
	}

	/**
	 * 获得 Actor
	 * @param playerId
	 * @param <T>
	 * @return
	 */
	public static <T extends AbstractUserActor> T getPlayerActor(long playerId) {
		return (T)datas.get(playerId);
	}
}
