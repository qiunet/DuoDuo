package org.qiunet.flash.handler.common.player;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.qiunet.cross.actor.CrossPlayerActor;
import org.qiunet.data.util.ServerConfig;
import org.qiunet.flash.handler.common.player.event.*;
import org.qiunet.flash.handler.common.player.observer.IPlayerDestroy;
import org.qiunet.flash.handler.common.player.proto.ReconnectInvalidPush;
import org.qiunet.flash.handler.context.response.push.DefaultBytesMessage;
import org.qiunet.flash.handler.netty.server.constants.CloseCause;
import org.qiunet.flash.handler.netty.server.constants.ServerConstants;
import org.qiunet.flash.handler.netty.server.param.adapter.message.ClockTickPush;
import org.qiunet.utils.async.future.DFuture;
import org.qiunet.utils.collection.enums.ForEachResult;
import org.qiunet.utils.listener.event.EventHandlerWeightType;
import org.qiunet.utils.listener.event.EventListener;
import org.qiunet.utils.listener.event.data.ServerShutdownEventData;

import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.function.Predicate;

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
	/*监听*/
	private List<IOnlineUserSizeChangeListener> changeListeners;
	/**
	 * 在线
	 */
	private static final Map<Long, AbstractUserActor> onlinePlayers = Maps.newConcurrentMap();
	/**
	 * 等待重连
	 */
	private static final Map<Long, WaitActor> waitReconnects = Maps.newConcurrentMap();

	@EventListener
	private void addPlayerActor(AuthEventData eventData) {
		AbstractUserActor userActor = eventData.getPlayer();
		Preconditions.checkState(userActor.isAuth());
		onlinePlayers.put(userActor.getId(), userActor);

		if (userActor.isPlayerActor()) {
			userActor.addMessage(p -> this.clockTick((PlayerActor) p));
		}
	}
	/**
	 * 玩家自主退出，会完整走登出流程
	 * @param actor 玩家
	 */
	public <T extends AbstractUserActor<T>> void playerQuit(T actor) {
		if (actor.isCrossPlayer()) {
			((CrossPlayerActor) actor).fireCrossEvent(CrossPlayerLogoutEvent.valueOf(ServerConfig.getServerId()));
		}

		actor.session.close(CloseCause.LOGOUT);
		this.destroyPlayer(actor);
	}
	/**
	 * 登出事件
	 * @param eventData
	 */
	@EventListener(EventHandlerWeightType.LESS)
	private void onLogout(PlayerActorLogoutEvent eventData) {
		PlayerActor actor = (PlayerActor) onlinePlayers.remove(eventData.getPlayer().getId());
		if (actor == null) {
			return;
		}

		triggerChangeListeners(false);
		// 清理 observers 避免重连重复监听.
		actor.getObserverSupport().clear(clz -> clz != IPlayerDestroy.class);

		actor.dataLoader().syncToDb();

		if (! eventData.getCause().needWaitConnect() || !actor.isAuth()) {
			this.destroyPlayer(actor);
			return;
		}

		if (actor.casWaitReconnect(false, true)) {
			// 给3分钟重连时间
			DFuture<Void> future = actor.scheduleMessage(p -> this.destroyPlayer(actor), 180, TimeUnit.SECONDS);
			waitReconnects.put(actor.getId(), new WaitActor(actor, future));
		}
	}
	/**
	 * 登出事件
	 * @param eventData
	 */
	@EventListener(EventHandlerWeightType.LESS)
	private void onLogout(CrossActorLogoutEvent eventData) {
		CrossPlayerActor actor = (CrossPlayerActor) onlinePlayers.remove(eventData.getPlayer().getId());
		if (actor == null) {
			return;
		}
		triggerChangeListeners(false);
		// 清理 observers 避免重连重复监听.
		actor.getObserverSupport().clear(clz -> clz != IPlayerDestroy.class);
		// 退出
		this.playerQuit(actor);
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
		if (waitActor == null || waitActor.actor.casWaitReconnect(true, false)) {
			currActor.sendMessage(ReconnectInvalidPush.getInstance());
			currActor.getSession().close(CloseCause.RECONNECT_INVALID);
			return null;
		}

		waitActor.actor.clearObservers();
		waitActor.actor.merge(currActor);
		waitActor.future.cancel(true);
		currActor.getSender().channel().attr(ServerConstants.MESSAGE_ACTOR_KEY).set(waitActor.actor);

		if (waitActor.actor.isNotNull(ServerConstants.INTEREST_MESSAGE_LIST)) {
			waitActor.actor.addMessage(this::resentInterestMsg);
		}

		onlinePlayers.put(playerId, waitActor.actor);
		triggerChangeListeners(true);
		currActor.destroy();

		this.clockTick(waitActor.actor);
		return waitActor.actor;
	}

	private void clockTick(PlayerActor actor) {
		if (! actor.getSession().isActive()) {
			return;
		}
		actor.sendMessage(ClockTickPush.valueOf());
		actor.scheduleMessage(this::clockTick, 1, TimeUnit.MINUTES);
	}

	private void resentInterestMsg(PlayerActor playerActor) {
		if (playerActor.waitReconnect()) {
			return;
		}

		List<DefaultBytesMessage> list = playerActor.getVal(ServerConstants.INTEREST_MESSAGE_LIST);
		list.forEach(msg -> playerActor.sendMessage(msg, false));
		playerActor.clear(ServerConstants.INTEREST_MESSAGE_LIST);
		playerActor.flush();
	}

	/**
	 * 销毁等待重连的对象.
	 * 注意. 仅销毁. 不会触发 IDestroyPlayer
	 * @param playerId
	 */
	public void destroyWaiter(long playerId) {
		WaitActor waitActor = waitReconnects.remove(playerId);
		if (waitActor == null) {
			return;
		}
		waitActor.future.cancel(true);
		waitActor.actor.clearObservers();
		waitActor.actor.destroy();
	}

	/**
	 * 玩家销毁， 销毁后，不可重连
	 * @param userActor
	 */
	private <T extends AbstractUserActor<T>> void destroyPlayer(T userActor) {
		if (userActor.isDestroyed()) {
			return;
		}

		userActor.getObserverSupport().syncFire(IPlayerDestroy.class, p -> p.destroyActor(userActor));
		if (userActor.isCrossPlayer() && userActor.getSender().isActive()) {
			((CrossPlayerActor) userActor).fireCrossEvent(CrossPlayerDestroyEvent.valueOf(ServerConfig.getServerId()));
		}
		waitReconnects.remove(userActor.getId());
		AbstractUserActor remove = onlinePlayers.remove(userActor.getId());
		if (remove != null) {
			triggerChangeListeners(false);
		}
		userActor.destroy();
	}
	/**
	 * 在线本服玩家数量
	 * @return 数量
	 */
	public int onlinePlayerSize(){
		return (int) onlinePlayers.values().stream().filter(AbstractUserActor::isPlayerActor).count();
	}
	/**
	 * 在线跨服玩家数量
	 * @return 数量
	 */
	public int crossPlayerSize(){
		return (int) onlinePlayers.values().stream().filter(AbstractUserActor::isCrossPlayer).count();
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
	 */
	public void foreach(Function<AbstractUserActor, ForEachResult> consume) {
		this.foreach(consume, null);
	}
	/**
	 * 遍历在线玩家.
	 * @param consume
	 */
	public void foreach(Function<AbstractUserActor, ForEachResult> consume, Predicate<AbstractUserActor> filter) {
		for (AbstractUserActor actor : onlinePlayers.values()) {
			if (filter != null && filter.test(actor)) {
				continue;
			}
			ForEachResult result = consume.apply(actor);
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
	public static PlayerActor getWaitReconnectPlayer(long playerId) {
		WaitActor waitActor = waitReconnects.get(playerId);
		return waitActor == null ? null : waitActor.actor;
	}

	private static class WaitActor {
		PlayerActor actor;
		DFuture<Void> future;

		public WaitActor(PlayerActor actor, DFuture<Void> future) {
			this.actor = actor;
			this.future = future;
		}
	}
	@EventListener(EventHandlerWeightType.HIGHEST)
	private void serverShutdown(ServerShutdownEventData event) {
		for (AbstractUserActor actor : onlinePlayers.values()) {
			actor.session.close(CloseCause.SERVER_SHUTDOWN);
		}
	}

	/**
	 * 添加变动监听
	 * @param listener
	 */
	public synchronized void addChangeListener(IOnlineUserSizeChangeListener listener) {
		if (this.changeListeners == null) {
			this.changeListeners = Lists.newCopyOnWriteArrayList();
		}
		this.changeListeners.add(listener);
	}

	/**
	 * 触发监听
	 * @return
	 */
	private void triggerChangeListeners(boolean add){
		if (changeListeners == null) {
			return;
		}
		changeListeners.forEach(listener -> listener.change(add));
	}
	/**
	 * 现在玩家变动监听
	 */
	@FunctionalInterface
	public interface IOnlineUserSizeChangeListener {
		/**
		 * @param add true 加 false 减
		 */
		void change(boolean add);
	}
}
