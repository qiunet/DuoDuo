package org.qiunet.flash.handler.common.player;

import com.google.common.collect.Maps;
import org.qiunet.cross.node.ServerNodeManager;
import org.qiunet.data.async.ISyncDbMessage;
import org.qiunet.data.db.entity.DbEntityList;
import org.qiunet.data.db.entity.IDbEntity;
import org.qiunet.data.db.loader.DbEntityBo;
import org.qiunet.data.db.loader.IPlayerDataLoader;
import org.qiunet.data.db.loader.PlayerDataLoader;
import org.qiunet.data.util.ServerType;
import org.qiunet.flash.handler.common.player.connect.PlayerCrossConnector;
import org.qiunet.flash.handler.common.player.event.BasePlayerEventData;
import org.qiunet.flash.handler.common.player.event.LoginSuccessEvent;
import org.qiunet.flash.handler.common.player.event.PlayerActorLogoutEvent;
import org.qiunet.flash.handler.common.player.event.UserEventData;
import org.qiunet.flash.handler.common.player.proto.PlayerLogoutPush;
import org.qiunet.flash.handler.context.response.push.IChannelMessage;
import org.qiunet.flash.handler.context.session.ISession;
import org.qiunet.flash.handler.netty.server.constants.CloseCause;
import org.qiunet.flash.handler.netty.server.kcp.event.KcpUsabilityEvent;
import org.qiunet.flash.handler.netty.server.kcp.observer.IKcpUsabilityChange;
import org.qiunet.flash.handler.netty.server.param.adapter.message.ClockTickPush;
import org.qiunet.utils.exceptions.CustomException;
import org.qiunet.utils.logger.LoggerType;

import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

/***
 * 玩家playerActor 的父类
 *
 * @author qiunet
 * 2020-10-21 10:08
 */
public final class PlayerActor extends AbstractUserActor<PlayerActor> implements ICrossStatusActor,
		IPlayer, IPlayerDataLoader, ISyncDbMessage {
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
	 * 登录成功的actor
	 */
	private boolean loginSuccess;
	/**
	 * 玩家ID
	 */
	private long playerId;
	/**
	 * 玩家平台ID
	 */
	private String openId;
	/**
	 * 玩家的构造
	 * @param session
	 */
	public PlayerActor(ISession session) {
		super(session);

		// kcp 变动通知推送
		this.attachObserver(IKcpUsabilityChange.class, (prepare -> this.allCrossEvent(KcpUsabilityEvent.valueOf(prepare))));
	}

	@Override
	protected void setSession(ISession session) {
		super.setSession(session);
	}

	/**
	 * 跨服session的心跳
	 */
	private void crossHeartBeat(){
		if (isDestroyed()){
			return;
		}

		this.scheduleMessage(p -> {
			if (isDestroyed()){
				return;
			}
			crossConnectors.values().forEach(PlayerCrossConnector::heartBeat);
			this.crossHeartBeat();
		}, 20, TimeUnit.SECONDS);

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

		dataLoader = new PlayerDataLoader(this, this, id);
		this.crossHeartBeat();
		this.playerId = id;
		this.clockTick();
	}

	private void clockTick() {
		if (this.getSession().isActive()) {
			this.sendMessage(ClockTickPush.valueOf());
		}

		this.scheduleMessage(p -> {
			this.clockTick();
		}, 2, TimeUnit.MINUTES);
	}

	/**
	 * 真的登录成功调用.
	 * 没有PlayerBo 都不算.
	 */
	public void loginSuccess() {
		new LoginSuccessEvent(this).fireEventHandler();
		this.sessionCloseListener();
		this.loginSuccess = true;
	}

	private void sessionCloseListener() {
		session.addCloseListener("PlayerActorLogoutEvent", (s, cause) -> {
			this.fireEvent(new PlayerActorLogoutEvent(cause));
			if (s.isActive() && cause.needLogoutPush()) {
				s.sendMessage(PlayerLogoutPush.valueOf(cause), true);
			}
		});

		session.addCloseListener("QuitAllCrossSession", (s, cause) -> {
			this.quitAllCross(cause);
		});
	}



	@Override
	public void destroy() {
		if (this.isDestroyed()) {
			return;
		}

		super.destroy();

		crossConnectors.values().forEach(c -> c.getSession().close(CloseCause.DESTROY));
		// 必须要登录成功后的actor销毁才执行这步. 否则有可能一个闲置的session关闭导致后面进来正常玩家的 dataLoader 被关闭
		if (loginSuccess && dataLoader != null) {
			dataLoader.unregister();
		}
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
	public <Do extends IDbEntity<?>, Bo extends DbEntityBo<Do>> Bo insertDo(Do entity) {
		if (! inSelfThread()) {
			throw new RuntimeException("Not in self thread!");
		}
		return IPlayerDataLoader.super.insertDo(entity);
	}

	@Override
	public <Data extends DbEntityBo<?>> Data getData(Class<Data> clazz) {
		if (! inSelfThread()) {
			throw new RuntimeException("Not in self thread!");
		}
		return IPlayerDataLoader.super.getData(clazz);
	}

	@Override
	public <SubKey, Bo extends DbEntityBo<Do>, Do extends DbEntityList<Long, SubKey>> Map<SubKey, Bo> getMapData(Class<Bo> clazz) {
		if (! inSelfThread()) {
			throw new RuntimeException("Not in self thread!");
		}
		return IPlayerDataLoader.super.getMapData(clazz);
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

	@Override
	public void syncBbMessage(Runnable runnable) {
		if (inSelfThread()) {
			runnable.run();
			return;
		}
		this.addMessage(h -> runnable.run());
	}
}
