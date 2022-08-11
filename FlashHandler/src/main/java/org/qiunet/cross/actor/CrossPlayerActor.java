package org.qiunet.cross.actor;

import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;
import io.netty.channel.ChannelFuture;
import org.qiunet.cross.actor.data.CrossData;
import org.qiunet.cross.actor.data.CrossDataGetter;
import org.qiunet.cross.actor.data.IUserTransferData;
import org.qiunet.cross.actor.message.Cross2PlayerMessage;
import org.qiunet.cross.event.BaseCrossPlayerEventData;
import org.qiunet.cross.event.CrossEventRequest;
import org.qiunet.flash.handler.common.player.AbstractUserActor;
import org.qiunet.flash.handler.common.player.event.CrossActorLogoutEvent;
import org.qiunet.flash.handler.common.player.event.LoginSuccessEvent;
import org.qiunet.flash.handler.common.player.event.UserEventData;
import org.qiunet.flash.handler.context.request.data.IChannelData;
import org.qiunet.flash.handler.context.session.ISession;
import org.qiunet.utils.listener.event.EventManager;

import java.util.Map;

/***
 * 跨服服务的playerActor
 *
 * @author qiunet
 * 2020-10-14 17:20
 */
public final class CrossPlayerActor extends AbstractUserActor<CrossPlayerActor> {
	/***
	 * 跨服的数据持有者
	 */
	private final Map<CrossData, CrossDataGetter> crossDataHolder = Maps.newConcurrentMap();
	/**
	 * kcp 可用
	 */
	private boolean kcpPrepare;
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
	public boolean isKcpSessionPrepare() {
		return kcpPrepare;
	}

	public void setKcpPrepare(boolean kcpPrepare) {
		this.kcpPrepare = kcpPrepare;
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

	/**
	 * 事件在当前服
	 * @param event
	 * @param <T>
	 */
	public  <T extends BaseCrossPlayerEventData> void fireEvent(T event) {
		super.fireEvent(event);
	}

	/**
	 * 事件发回玩家数据所在server
	 * @param eventData
	 * @param <T>
	 */
	public  <T extends UserEventData> void fireCrossEvent(T eventData) {
		Preconditions.checkState(isAuth(), "Need auth!");

		CrossEventRequest request = CrossEventRequest.valueOf(eventData);
		session.sendMessage(request.buildChannelMessage());
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
	 * 调用该接口. 会直接转发给客户端
	 * @param channelData
	 */
	@Override
	public ChannelFuture sendMessage(IChannelData channelData) {
		return this.sendMessage(channelData, false);
	}

	@Override
	public ChannelFuture sendMessage(IChannelData channelData, boolean flush) {
		return super.sendMessage(Cross2PlayerMessage.valueOf(channelData, flush), flush);
	}

	@Override
	public ChannelFuture sendKcpMessage(IChannelData channelData) {
		// kcp 要求实时. 直接发送出去
		return super.sendMessage(Cross2PlayerMessage.valueOf(channelData, true, true), true);
	}
}
