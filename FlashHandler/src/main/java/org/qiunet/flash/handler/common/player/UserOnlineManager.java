package org.qiunet.flash.handler.common.player;

import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;
import org.qiunet.cross.actor.CrossPlayerActor;
import org.qiunet.data.util.ServerConfig;
import org.qiunet.flash.handler.common.player.event.AuthEventData;
import org.qiunet.flash.handler.common.player.event.CrossPlayerDestroyEvent;
import org.qiunet.flash.handler.common.player.event.CrossPlayerLogoutEvent;
import org.qiunet.flash.handler.common.player.event.PlayerLogoutEventData;
import org.qiunet.flash.handler.common.player.observer.IPlayerDestroy;
import org.qiunet.flash.handler.netty.server.constants.CloseCause;
import org.qiunet.flash.handler.netty.server.constants.ServerConstants;
import org.qiunet.utils.async.future.DFuture;
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
	private static final Map<Long, WaitActor> waitReconnects = Maps.newConcurrentMap();

	@EventListener
	private <T extends AbstractUserActor<T>> void addPlayerActor(AuthEventData<T> eventData) {
		AbstractUserActor<T> userActor = eventData.getPlayer();
		Preconditions.checkState(userActor.isAuth());
		onlinePlayers.put(userActor.getId(), userActor);
	}
	/**
	 * 玩家退出，
	 * @param actor 玩家
	 */
	public <T extends AbstractUserActor<T>> void playerQuit(T actor) {
		if (actor instanceof CrossPlayerActor) {
			((CrossPlayerActor) actor).fireCrossEvent(CrossPlayerLogoutEvent.valueOf(ServerConfig.getServerId()));
		}
		this.destroyPlayer(actor);

		actor.session.close(CloseCause.LOGOUT);
	}
	/**
	 * 登出事件
	 * @param eventData
	 */
	@EventListener(EventHandlerWeightType.LESS)
	private <T extends AbstractUserActor<T>> void onLogout(PlayerLogoutEventData<T> eventData) {
		AbstractUserActor<T> userActor = eventData.getPlayer();
		T actor = (T) onlinePlayers.remove(userActor.getId());
		if (actor == null) {
			return;
		}

		// CrossPlayerActor 如果断连. 由playerActor维护心跳.
		if (actor instanceof CrossPlayerActor) {
			return;
		}

		((PlayerActor) actor).dataLoader().syncToDb();
		if (eventData.getCause().needWaitConnect() && userActor.isAuth()) {
			// 给3分钟重连时间
			DFuture<Void> future = actor.scheduleMessage(p -> this.destroyPlayer(actor), 3, TimeUnit.MINUTES);
			waitReconnects.put(actor.getId(), new WaitActor(((PlayerActor) actor), future));
		}
	}
	/**
	 * 重连
	 * 重连需要把新的session换到旧的里面。 把channel里面换成旧的
	 * @param playerId 玩家id
	 * @param currActor 当前的actor
	 * @return null 说明不能重连了. 否则之后使用返回的actor进行操作.
	 */
	public PlayerActor reconnect(long playerId, PlayerActor currActor) {
		WaitActor waitActor = waitReconnects.remove(playerId);
		if (waitActor == null) {
			return null;
		}
		waitActor.actor.merge(currActor);
		waitActor.future.cancel(true);
		currActor.getSender().channel().attr(ServerConstants.MESSAGE_ACTOR_KEY).set(waitActor.actor);

		onlinePlayers.put(playerId, waitActor.actor);
		currActor.destroy();

		return waitActor.actor;
	}
	/**
	 * 玩家销毁， 销毁后，不可重连
	 * @param userActor
	 */
	private <T extends AbstractUserActor<T>> void destroyPlayer(T userActor) {
		userActor.getObserverSupport().syncFire(IPlayerDestroy.class, p -> p.destroyActor(userActor));
		if (userActor instanceof CrossPlayerActor && userActor.getSender().isActive()) {
			((CrossPlayerActor) userActor).fireCrossEvent(CrossPlayerDestroyEvent.valueOf(ServerConfig.getServerId()));
		}
		onlinePlayers.remove(userActor.getId());
		waitReconnects.remove(userActor.getId());
		userActor.destroy();
	}
	/**
	 * 在线本服玩家数量
	 * @return 数量
	 */
	public int onlinePlayerSize(){
		return (int) onlinePlayers.values().stream().filter(actor -> actor instanceof PlayerActor).count();
	}
	/**
	 * 在线跨服玩家数量
	 * @return 数量
	 */
	public int crossPlayerSize(){
		return (int) onlinePlayers.values().stream().filter(actor -> ! (actor instanceof PlayerActor)).count();
	}

	/**
	 * 所有在线的数量
	 * @return
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
		WaitActor waitActor = waitReconnects.get(playerId);
		return waitActor == null ? null : (T)waitActor.actor;
	}

	private static class WaitActor {
		PlayerActor actor;
		DFuture<Void> future;

		public WaitActor(PlayerActor actor, DFuture<Void> future) {
			this.actor = actor;
			this.future = future;
		}
	}
}
