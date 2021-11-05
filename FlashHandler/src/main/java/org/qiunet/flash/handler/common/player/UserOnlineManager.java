package org.qiunet.flash.handler.common.player;

import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;
import org.qiunet.flash.handler.common.player.event.AuthEventData;
import org.qiunet.flash.handler.common.player.event.PlayerLogoutEventData;
import org.qiunet.flash.handler.common.player.observer.IPlayerDestroy;
import org.qiunet.flash.handler.netty.server.constants.CloseCause;
import org.qiunet.flash.handler.netty.server.constants.ServerConstants;
import org.qiunet.utils.collection.enums.ForEachResult;
import org.qiunet.utils.listener.event.EventHandlerWeightType;
import org.qiunet.utils.listener.event.EventListener;

import java.util.Map;
import java.util.concurrent.TimeUnit;
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
	/**
	 * 在线
	 */
	private static final Map<Long, AbstractUserActor> onlinePlayers = Maps.newConcurrentMap();
	/**
	 * 等待重连
	 */
	private static final Map<Long, AbstractUserActor> waitReconnects = Maps.newConcurrentMap();

	@EventListener
	private <T extends AbstractUserActor<T>> void addPlayerActor(AuthEventData<T> eventData) {
		AbstractUserActor<T> userActor = eventData.getPlayer();
		Preconditions.checkState(userActor.isAuth());
		userActor.getSession().addCloseListener(cause -> onlinePlayers.remove(userActor.getId()));
		onlinePlayers.put(userActor.getId(), userActor);
	}
	/**
	 * 玩家退出，
	 * @param actor 玩家
	 */
	public <T extends AbstractUserActor<T>> void playerQuit(T actor) {
		this.destroyPlayer(actor, true);
		actor.getSession().close(CloseCause.LOGOUT);
	}
	/**
	 * 登出事件
	 * @param eventData
	 */
	@EventListener(EventHandlerWeightType.LESS)
	private <T extends AbstractUserActor<T>> void onLogout(PlayerLogoutEventData<T> eventData) {
		AbstractUserActor<T> userActor = eventData.getPlayer();
		if (eventData.getCause() != CloseCause.LOGOUT && userActor.isAuth()) {
			T actor = (T) onlinePlayers.remove(userActor.getId());
			if (actor != null) {
				waitReconnects.put(actor.getId(), actor);
				// 给10分钟都重连时间
				actor.scheduleMessage(p -> this.destroyPlayer(actor, false), 10, TimeUnit.MINUTES);
			}
		}
	}
	/**
	 * 重连
	 * 重连需要把新的session换到旧的里面。 把channel里面换成旧的
	 * @param playerId 玩家id
	 * @param currActor 当前的actor
	 * @return null 说明不能重连了. 否则之后使用返回的actor进行操作.
	 */
	public <T extends AbstractUserActor<T>> T reconnect(long playerId, T currActor) {
		T actor = (T) waitReconnects.remove(playerId);
		if (actor == null) {
			return null;
		}
		actor.merge(currActor);

		actor.setSession(currActor.session);
		currActor.getSession().channel().attr(ServerConstants.MESSAGE_ACTOR_KEY).set(actor);

		onlinePlayers.put(playerId, actor);
		return actor;
	}
	/**
	 * 玩家销毁， 销毁后，不可重连
	 * @param userActor
	 */
	private <T extends AbstractUserActor<T>> void destroyPlayer(T userActor, boolean quit) {
		userActor.getObserverSupport().syncFire(IPlayerDestroy.class, p -> p.destroyActor(userActor));
		if (quit) {
			onlinePlayers.remove(userActor.getId());
		}else {
			waitReconnects.remove(userActor.getId());
		}
	}
	/**
	 * 在线玩家数量
	 * @return 数量
	 */
	public int onlineSize(){
		return onlinePlayers.size();
	}
	/**
	 * 遍历在线玩家.
	 * @param consume
	 * @param <T>
	 */
	public <T extends AbstractUserActor<T>> void foreach(Function<T, ForEachResult> consume) {
		for (AbstractUserActor actor : onlinePlayers.values()) {
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
	public static <T extends AbstractUserActor<T>> T getPlayerActor(long playerId) {
		return (T) onlinePlayers.get(playerId);
	}
	/**
	 * 得到等待重连的player
	 * @param playerId 玩家id
	 * @return playerActor
	 */
	public static <T extends AbstractUserActor<T>> T getWaitReconnectPlayer(long playerId) {
		return (T) waitReconnects.get(playerId);
	}
}
