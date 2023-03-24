package org.qiunet.flash.handler.common.player;

import com.google.common.collect.Maps;
import org.qiunet.cross.event.BaseCrossPlayerEvent;
import org.qiunet.cross.node.ServerNodeManager;
import org.qiunet.data.async.ISyncDbMessage;
import org.qiunet.data.db.entity.DbEntityList;
import org.qiunet.data.db.entity.IDbEntity;
import org.qiunet.data.db.loader.DbEntityBo;
import org.qiunet.data.db.loader.IPlayerDataLoader;
import org.qiunet.data.db.loader.PlayerDataLoader;
import org.qiunet.flash.handler.common.player.connect.PlayerCrossConnector;
import org.qiunet.flash.handler.common.player.event.BasePlayerEvent;
import org.qiunet.flash.handler.common.player.event.LoginSuccessEvent;
import org.qiunet.flash.handler.common.player.event.PlayerActorLogoutEvent;
import org.qiunet.flash.handler.common.player.proto.PlayerLogoutPush;
import org.qiunet.flash.handler.context.response.push.IChannelMessage;
import org.qiunet.flash.handler.context.session.DSession;
import org.qiunet.flash.handler.context.session.ISession;
import org.qiunet.flash.handler.netty.server.config.adapter.message.ClockTickPush;
import org.qiunet.flash.handler.netty.server.constants.CloseCause;
import org.qiunet.flash.handler.netty.server.kcp.event.KcpUsabilityEvent;
import org.qiunet.flash.handler.netty.server.kcp.observer.IKcpUsabilityChange;
import org.qiunet.utils.exceptions.CustomException;
import org.qiunet.utils.logger.LoggerType;

import java.util.Map;
import java.util.Stack;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;

/***
 * 玩家playerActor 的父类
 *
 * @author qiunet
 * 2020-10-21 10:08
 */
public final class PlayerActor extends AbstractUserActor<PlayerActor> implements ICrossStatusActor,
		IPlayerFireEvent<BasePlayerEvent, BaseCrossPlayerEvent, PlayerActor>, IPlayer, IPlayerDataLoader, ISyncDbMessage {
	/**
	 * 跨服的连接管理
	 */
	private final Map<Integer, PlayerCrossConnector> crossConnectors = Maps.newHashMap();
	/**
	 * 保证玩家跨服从一个服务退出. 回到前一个服务
	 */
	private final Stack<Integer> crosssServerStack = new Stack<>();
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
	private int currentCrossServerId;
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

	@Override
	public <E extends BaseCrossPlayerEvent> void allCrossEvent(E event) {
		crossConnectors.values().forEach(crossConnector -> {
			crossConnector.fireCrossEvent(event);
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
	public void crossToServer(int serverId, Consumer<Boolean> resultCallback) {
		if (inSelfThread()) {
			this.crossToServer0(serverId, resultCallback);
		}else {
			this.addMessage(p -> p.crossToServer0(serverId, resultCallback));
		}
	}

	/**
	 * 跨服
	 * @param serverId
	 * @param resultCallback 结果回调
	 */
	private void crossToServer0(int serverId, Consumer<Boolean> resultCallback) {
		if (isCrossStatus(serverId)) {
			throw new CustomException("Current is cross to a [{}] server!", serverId);
		}

		if (serverId == ServerNodeManager.getCurrServerId()) {
			throw new CustomException("Can not cross to self!");
		}

		PlayerCrossConnector connector = new PlayerCrossConnector(this, serverId);
		connector.connect(result -> {
			if (result) {
				LoggerType.DUODUO_FLASH_HANDLER.info("player {} cross to serverId {} success!", this.getId(), serverId);
				this.crosssServerStack.push(this.currentCrossServerId);
				crossConnectors.put(serverId, connector);
				this.currentCrossServerId = serverId;
			}else {
				LoggerType.DUODUO_FLASH_HANDLER.error("Player {} cross to serverId {} FAIL!", this.getId(), serverId);
			}

			if (resultCallback != null) {
				resultCallback.accept(result);
			}
		});

	}

	@Override
	public int currentCrossServerId() {
		return currentCrossServerId;
	}

	@Override
	public void quitCurrentCross(CloseCause cause) {
		if (! isCrossStatus()) {
			return;
		}
		this.quitCross(this.currentCrossServerId, cause);
	}

	/**
	 * 退出指定类型的跨服
	 * @param serverId
	 * @param cause
	 */
	public void quitCross(int serverId, CloseCause cause) {
		if (! isCrossStatus()) {
			return;
		}

		if (currentCrossServerId != serverId) {
			throw new CustomException("not cross to server {}", serverId);
		}

		PlayerCrossConnector playerCrossConnector = crossConnectors.remove(serverId);
		if (playerCrossConnector == null) {
			return;
		}

		LoggerType.DUODUO_FLASH_HANDLER.info("Player: {} quit cross server id {}", this.getId(), serverId);
		this.currentCrossServerId = crosssServerStack.pop();
		playerCrossConnector.getSession().close(cause);
	}

	@Override
	public boolean isCrossStatus(int serverId) {
		return crossConnectors.containsKey(serverId);
	}

	@Override
	public void sendCrossMessage(IChannelMessage<?> channelMessage) {
		if (currentCrossServerId == 0) {
			throw new CustomException("Current not cross to any server");
		}
		crossConnectors.get(currentCrossServerId).sendMessage(channelMessage, true);
	}

	@Override
	public ISession crossSession() {
		if (currentCrossServerId == 0) {
			throw new CustomException("Current not cross to any server");
		}
		return crossConnectors.get(currentCrossServerId).getSession();
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
		handler.dataLoader.unregister();
		handler.dataLoader = null;
		dataLoader.register();
	}

	/**
	 * 对当前跨服的服务器发送跨服事件
	 * @param event
	 */
	@Override
	public void fireCrossEvent(BaseCrossPlayerEvent event) {
		crossConnectors.get(currentCrossServerId).fireCrossEvent(event);
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
	public DSession getSession() {
		return (DSession) super.getSession();
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
		if (inSelfThread() || isDestroyed() || ! this.addMessage(h -> runnable.run())) {
			runnable.run();
		}
	}
}
