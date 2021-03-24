package org.qiunet.flash.handler.common.player;

import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;
import org.qiunet.flash.handler.common.player.event.AuthEventData;
import org.qiunet.flash.handler.common.player.event.PlayerLogoutEventData;
import org.qiunet.listener.event.EventHandlerWeightType;
import org.qiunet.listener.event.EventListener;
import org.qiunet.utils.collection.enums.ForEachResult;

import java.util.Map;
import java.util.function.Function;

/***
 * 用户的actor管理.
 * 包括功能服 和 跨服的.
 * 在功能服. 管理的是用户的真实连接.
 * 在跨服. 管理的是功能服代理的连接
 *
 * @author qiunet
 * 2020-10-16 10:25
 */
public enum UserOnlineManager {
	instance;

	private static final Map<Long, AbstractUserActor> datas = Maps.newConcurrentMap();

	@EventListener
	private void addPlayerActor(AuthEventData eventData) {
		AbstractUserActor userActor = eventData.getPlayer();
		Preconditions.checkState(userActor.isAuth());
		userActor.getSession().addCloseListener(cause -> datas.remove(userActor.getId()));
		datas.put(userActor.getId(), userActor);
	}

	/**
	 * 登出事件
	 * @param eventData
	 */
	@EventListener(EventHandlerWeightType.LESS)
	private void onLogout(PlayerLogoutEventData eventData) {
		eventData.getPlayer().destroy();
	}

	/**
	 * 遍历在线玩家.
	 * @param consume
	 * @param <T>
	 */
	public <T extends AbstractUserActor<T>> void foreach(Function<T, ForEachResult> consume) {
		for (AbstractUserActor actor : datas.values()) {
			ForEachResult result = consume.apply((T) actor);
			if (result == ForEachResult.BREAK) {
				break;
			}
		}
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
