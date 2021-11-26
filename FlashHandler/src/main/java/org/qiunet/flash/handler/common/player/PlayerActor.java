package org.qiunet.flash.handler.common.player;

import com.google.common.collect.Maps;
import org.qiunet.cross.event.BaseCrossPlayerEventData;
import org.qiunet.cross.event.CrossEventManager;
import org.qiunet.cross.node.ServerNodeManager;
import org.qiunet.data.db.loader.IPlayerDataLoader;
import org.qiunet.data.db.loader.PlayerDataLoader;
import org.qiunet.data.util.ServerType;
import org.qiunet.flash.handler.common.player.connect.PlayerCrossConnector;
import org.qiunet.flash.handler.common.player.event.AuthEventData;
import org.qiunet.flash.handler.common.player.event.BasePlayerEventData;
import org.qiunet.flash.handler.context.request.data.IChannelData;
import org.qiunet.flash.handler.context.session.DSession;
import org.qiunet.flash.handler.netty.server.constants.CloseCause;
import org.qiunet.utils.exceptions.CustomException;

import java.util.Map;

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
	private static final Map<ServerType, PlayerCrossConnector> crossConnectors = Maps.newEnumMap(ServerType.class);
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
	 * 玩家的构造
	 * @param session
	 */
	public PlayerActor(DSession session) {
		super(session);
		session.addCloseListener(this::quitAllCross);
	}

	/**
	 * 退出所有跨服
	 */
	@Override
	public void quitAllCross(CloseCause cause) {
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
		if (playerCrossConnector == null) {
			return;
		}
		playerCrossConnector.getSession().close(cause);
	}

	@Override
	public void switchCross(ServerType serverType) {
		if (! isCrossStatus(serverType)) {
			throw new CustomException("Current not cross a [{}] server!", serverType);
		}
		this.crossServerType = serverType;
	}

	@Override
	public boolean isCrossStatus(ServerType serverType) {
		return crossConnectors.containsKey(serverType);
	}

	@Override
	public void sendCrossMessage(IChannelData channelData) {
		if (crossServerType == null) {
			throw new CustomException("Current not cross to any server");
		}
		crossConnectors.get(crossServerType).sendMessage(channelData);
	}

	/**
	 * 触发事件
	 * @param eventData
	 * @param <D>
	 */
	public <D extends BasePlayerEventData> void fireEvent(D eventData) {
		super.fireEvent(eventData);
	}

	@Override
	public void auth(long id) {
		if (isAuth()) {
			return;
		}
		this.playerId = id;
		dataLoader = new PlayerDataLoader(id);
		new AuthEventData<>(this).fireEventHandler();
	}

	@Override
	public void destroy() {
		if (dataLoader != null) {
			dataLoader.destroy();
		}
		super.destroy();
	}

	/**
	 * 对当前跨服的服务器发送跨服事件
	 * @param eventData
	 * @param <D>
	 */
	public <D extends BaseCrossPlayerEventData> void fireCrossEvent(D eventData) {
		CrossEventManager.fireCrossEvent(getId(), crossConnectors.get(crossServerType).getSession(), eventData);
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
	public long getId() {
		return playerId;
	}

	@Override
	public PlayerDataLoader dataLoader() {
		return dataLoader;
	}
}
