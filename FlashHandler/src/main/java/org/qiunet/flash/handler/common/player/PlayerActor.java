package org.qiunet.flash.handler.common.player;

import com.google.common.collect.Maps;
import org.qiunet.cross.node.ServerNodeManager;
import org.qiunet.data.db.loader.IPlayerDataLoader;
import org.qiunet.data.db.loader.PlayerDataLoader;
import org.qiunet.data.util.ServerType;
import org.qiunet.flash.handler.common.player.connect.PlayerCrossConnector;
import org.qiunet.flash.handler.common.player.event.AuthEventData;
import org.qiunet.flash.handler.common.player.event.BasePlayerEventData;
import org.qiunet.flash.handler.common.player.event.PlayerActorLogoutEvent;
import org.qiunet.flash.handler.common.player.event.UserEventData;
import org.qiunet.flash.handler.common.player.proto.PlayerLogoutPush;
import org.qiunet.flash.handler.context.response.push.IChannelMessage;
import org.qiunet.flash.handler.context.session.ISession;
import org.qiunet.flash.handler.netty.server.constants.CloseCause;
import org.qiunet.flash.handler.netty.server.kcp.event.KcpUsabilityEvent;
import org.qiunet.flash.handler.netty.server.kcp.observer.IKcpUsabilityChange;
import org.qiunet.utils.exceptions.CustomException;
import org.qiunet.utils.logger.LoggerType;

import java.util.Map;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

/***
 * 玩家playerActor 的父类
 *
 * @author qiunet
 * 2020-10-21 10:08
 */
public final class PlayerActor extends AbstractUserActor<PlayerActor> implements ICrossStatusActor, IPlayer, IPlayerDataLoader {
	/**
	 * 跨服的连接管理
	 */
	private final Map<ServerType, PlayerCrossConnector> crossConnectors = Maps.newEnumMap(ServerType.class);
	/**
	 * 等待重连状态
	 * 该状态下, 跨服的包不往下发.
	 * 监听兴趣包. 保存 . 重连后, 一起下发.
	 */
	private final AtomicBoolean waitReconnect = new AtomicBoolean();
	/**
	 * 玩家的数据加载器
	 */
	private PlayerDataLoader dataLoader;
	/**
	 * 当前跨服类型
	 */
	private ServerType crossServerType;
	/**
	 * 玩家ID
	 */
	private long playerId;
	/**
	 * 玩家平台ID
	 */
	private String openId;
	/**
	 * 跨服心跳feature
	 */
	private final Future<?> beatFuture;
	/**
	 * 玩家的构造
	 * @param session
	 */
	public PlayerActor(ISession session) {
		super(session);

		this.beatFuture = this.scheduleAtFixedRate("跨服Session心跳", p -> crossHeartBeat(), 10, 60, TimeUnit.SECONDS);

		// kcp 变动通知推送
		this.attachObserver(IKcpUsabilityChange.class, (prepare -> this.allCrossEvent(KcpUsabilityEvent.valueOf(prepare))));
	}

	@Override
	protected void setSession(ISession session) {
		super.setSession(session);

		session.addCloseListener((s, cause) -> {
			this.fireEvent(new PlayerActorLogoutEvent(cause));
		});

		session.addCloseListener((s, cause) -> {
			if (s.isActive()) {
				s.sendMessage(PlayerLogoutPush.valueOf(cause), true);
			}
			this.quitAllCross(cause);
		});
	}

	/**
	 * 跨服session的心跳
	 */
	private void crossHeartBeat(){
		crossConnectors.values().forEach(PlayerCrossConnector::heartBeat);
	}
	/**
	 * 给所有的跨服连接发送事件
	 * @param eventData
	 */
	private void allCrossEvent(UserEventData eventData) {
		crossConnectors.values().forEach(crossConnector -> {
			crossConnector.fireCrossEvent(eventData);
		});
	}

	/**
	 * 退出所有跨服
	 */
	@Override
	public void quitAllCross(CloseCause cause) {
		if (cause.needWaitConnect()) {
			return;
		}
		crossConnectors.keySet().forEach(type -> this.quitCross(type, cause));
	}

	@Override
	public void crossToServer(int serverId) {
		ServerType serverType = ServerType.getServerType(serverId);
		if (isCrossStatus(serverType)) {
			throw new CustomException("Current is cross to a [{}] server!", serverType);
		}

		if (serverId == ServerNodeManager.getCurrServerId()) {
			throw new CustomException("Can not cross to self!");
		}
		LoggerType.DUODUO_FLASH_HANDLER.info("player {} cross to serverId {}", this.getId(), serverId);
		crossConnectors.computeIfAbsent(serverType, key -> new PlayerCrossConnector(this, serverId));
		this.crossServerType = serverType;
	}

	@Override
	public ServerType currentCrossType() {
		return crossServerType;
	}

	/**
	 * 退出指定类型的跨服
	 * @param serverType
	 * @param cause
	 */
	@Override
	public void quitCross(ServerType serverType, CloseCause cause) {
		PlayerCrossConnector playerCrossConnector = crossConnectors.remove(serverType);
		LoggerType.DUODUO_FLASH_HANDLER.info("Player: {} quit cross server type {}", this.getId(), serverType);
		if (playerCrossConnector == null) {
			return;
		}
		playerCrossConnector.getSession().close(cause);
		if (crossServerType == serverType) {
			this.switchCross(null);
		}
	}

	@Override
	public void switchCross(ServerType serverType) {
		if (serverType != null && ! isCrossStatus(serverType)) {
			throw new CustomException("Current not cross a [{}] server!", serverType);
		}
		this.crossServerType = serverType;
	}

	@Override
	public boolean isCrossStatus(ServerType serverType) {
		return crossConnectors.containsKey(serverType);
	}

	@Override
	public void sendCrossMessage(IChannelMessage<?> channelMessage) {
		if (crossServerType == null) {
			throw new CustomException("Current not cross to any server");
		}
		crossConnectors.get(crossServerType).sendMessage(channelMessage, true);
	}

	@Override
	public ISession crossSession() {
		if (crossServerType == null) {
			throw new CustomException("Current not cross to any server");
		}
		return crossConnectors.get(crossServerType).getSession();
	}

	@Override
	public void auth(long id) {
		if (isAuth()) {
			return;
		}
		this.playerId = id;
		dataLoader = new PlayerDataLoader(id);
		new AuthEventData(this).fireEventHandler();
	}

	@Override
	public void destroy() {
		if (this.isDestroyed()) {
			return;
		}

		super.destroy();

		crossConnectors.values().forEach(c -> c.getSession().close(CloseCause.DESTROY));
		if (dataLoader != null) {
			dataLoader.unregister();
		}
		this.beatFuture.cancel(false);
	}

	/**
	 * 合并
	 * @param handler
	 */
	public void merge(PlayerActor handler) {
		if (playerId != handler.playerId) {
			throw new CustomException("PlayerId not the same!");
		}

		handler.session.clearCloseListener();
		this.setSession(handler.session);
		super.merge(handler);

		handler.dataLoader.unregister();
		handler.dataLoader = null;
		dataLoader.register();
	}

	/**
	 * 对当前跨服的服务器发送跨服事件
	 * @param event
	 * @param <D>
	 */
	public <D extends UserEventData> void fireCrossEvent(D event) {
		crossConnectors.get(crossServerType).fireCrossEvent(event);
	}

	/**
	 * 触发本服事件
	 *
	 * @param event
	 * @param <D>
	 */
	public <D extends BasePlayerEventData> void fireEvent(D event) {
		super.fireEvent(event);
	}

	public long getPlayerId() {
		return playerId;
	}

	public String getOpenId() {
		return openId;
	}

	public void setOpenId(String openId) {
		this.openId = openId;
	}

	@Override
	public boolean isCrossPlayer() {
		return false;
	}

	@Override
	public long getId() {
		return playerId;
	}

	@Override
	public PlayerDataLoader dataLoader() {
		return dataLoader;
	}

	public boolean waitReconnect(){
		return waitReconnect.get();
	}

	public boolean casWaitReconnect(boolean expect, boolean val) {
		return this.waitReconnect.compareAndSet(expect, val);
	}
}
