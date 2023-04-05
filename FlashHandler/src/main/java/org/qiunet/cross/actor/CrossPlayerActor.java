package org.qiunet.cross.actor;

import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;
import org.qiunet.cross.actor.data.CrossData;
import org.qiunet.cross.actor.data.CrossDataGetter;
import org.qiunet.cross.actor.data.IUserTransferData;
import org.qiunet.cross.actor.data._CrossDataNeedUpdateEvent;
import org.qiunet.cross.event.BaseCrossPlayerEvent;
import org.qiunet.cross.event.CrossEventRequest;
import org.qiunet.flash.handler.common.player.AbstractUserActor;
import org.qiunet.flash.handler.common.player.IPlayerFireEvent;
import org.qiunet.flash.handler.common.player.event.BasePlayerEvent;
import org.qiunet.flash.handler.common.player.event.CrossActorLogoutEvent;
import org.qiunet.flash.handler.common.player.event.LoginSuccessEvent;
import org.qiunet.flash.handler.context.session.ISession;
import org.qiunet.utils.exceptions.CustomException;
import org.qiunet.utils.json.JsonUtil;
import org.qiunet.utils.listener.event.EventManager;

import java.util.Map;

/***
 * 跨服服务的playerActor
 *
 * @author qiunet
 * 2020-10-14 17:20
 */
public final class CrossPlayerActor extends AbstractUserActor<CrossPlayerActor>
		implements IPlayerFireEvent<BaseCrossPlayerEvent, BasePlayerEvent, CrossPlayerActor> {
	/***
	 * 跨服的数据持有者
	 */
	private final Map<CrossData, CrossDataGetter> crossDataHolder = Maps.newConcurrentMap();
	/**
	 * 玩家id
	 */
	private long playerId;
	/**
	 * 玩家的服务器
	 */
	private int serverId;


	public CrossPlayerActor(ISession session) {
		super(session);
	}

	@Override
	protected void setSession(ISession session) {
		super.setSession(session);

		session.addCloseListener("CrossActorLogoutEvent", (s, cause) -> {
			this.fireEvent(new CrossActorLogoutEvent(cause));
		});
	}

	@Override
	public boolean isCrossPlayer() {
		return true;
	}

	@Override
	public void auth(long playerId) {
		this.playerId = playerId;
		EventManager.fireEventHandler(new LoginSuccessEvent(this));
	}

	@Override
	public void fireCrossEvent(BasePlayerEvent event) {
		Preconditions.checkState(isAuth(), "Need auth!");

		CrossEventRequest request = CrossEventRequest.valueOf(event);
		session.sendMessage(request.buildChannelMessage(), true);
	}

	public long getPlayerId() {
		return playerId;
	}

	public void setPlayerId(long playerId) {
		this.playerId = playerId;
	}

	@Override
	public long getId() {
		return playerId;
	}

	public int getServerId() {
		return serverId;
	}

	public void setServerId(int serverId) {
		this.serverId = serverId;
	}
	/**
	 * 获得CrossData定义的数据
	 * @param key
	 * @param <Data>
	 * @return
	 */
	public <Data extends IUserTransferData> Data getCrossData(CrossData<Data> key) {
		CrossDataGetter<Data> getter = crossDataHolder.computeIfAbsent(key, key0 -> new CrossDataGetter(this, key0));
		return getter.get();
	}

	/**
	 * update
	 * @param key
	 * @param <Data>
	 */
	public <Data extends IUserTransferData> void updateCrossData(CrossData<Data> key) {
		CrossDataGetter getter = crossDataHolder.get(key);
		if (getter == null || ! getter.isPresent()) {
			throw new CustomException("cross data not present!");
		}
		String jsonString = JsonUtil.toJsonString(getter.get());
		this.fireCrossEvent(_CrossDataNeedUpdateEvent.valueOf(key.name(), jsonString));
	}

	@Override
	public ISession getKcpSession() {
		// 并非真是依靠kcpSession发送.
		return session;
	}

	@Override
	protected void exceptionHandle(Exception e) {
		logger.error("Cross player actor exception:", e);
	}
}
