package org.qiunet.cross.actor;

import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;
import org.qiunet.cross.actor.data.BaseCrossTransferData;
import org.qiunet.cross.actor.data.CrossData;
import org.qiunet.cross.actor.data.CrossDataGetter;
import org.qiunet.cross.actor.message.Cross2PlayerResponse;
import org.qiunet.cross.event.BaseCrossPlayerEventData;
import org.qiunet.cross.event.CrossEventManager;
import org.qiunet.flash.handler.common.player.AbstractUserActor;
import org.qiunet.flash.handler.common.player.event.AuthEventData;
import org.qiunet.flash.handler.common.player.event.BasePlayerEventData;
import org.qiunet.flash.handler.context.request.data.pb.IpbResponseData;
import org.qiunet.flash.handler.context.session.DSession;
import org.qiunet.listener.event.EventManager;

import java.util.Map;

/***
 * 跨服服务的playerActor
 *
 * @author qiunet
 * 2020-10-14 17:20
 */
public class CrossPlayerActor extends AbstractUserActor<CrossPlayerActor> {
	/***
	 * 跨服的数据持有者
	 */
	private Map<CrossData, CrossDataGetter> crossDataHolder = Maps.newConcurrentMap();
	/**
	 * 玩家id
	 */
	private long playerId;
	/**
	 * 玩家的服务器
	 */
	private int serverId;


	public CrossPlayerActor(DSession session) {
		super(session);
	}

	@Override
	public boolean isCrossStatus() {
		// 进入跨服了. 必然是跨服状态. 在另一个服有PlayerActor
		return true;
	}

	@Override
	public void auth(long playerId) {
		this.playerId = playerId;
		EventManager.fireEventHandler(new AuthEventData<>(this));
	}

	public  <T extends BaseCrossPlayerEventData> void fireEvent(T eventData) {
		eventData.setPlayer(this);
		EventManager.fireEventHandler(eventData);
	}

	public  <T extends BasePlayerEventData> void fireEvent(T eventData) {
		Preconditions.checkState(isAuth(), "Need auth!");
		CrossEventManager.fireCrossEvent(getPlayerId(), getSession(), eventData);
	}

	/**
	 * 调用该接口. 会直接转发给客户端
	 * @param responseData
	 */
	public void sendMessage(IpbResponseData responseData) {
		this.send(Cross2PlayerResponse.valueOf(responseData).buildResponseMessage());
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
	public <Data extends BaseCrossTransferData> Data getCrossData(CrossData<Data> key) {
		CrossDataGetter<Data> getter = crossDataHolder.computeIfAbsent(key, key0 -> new CrossDataGetter(this, key0));
		return getter.get();
	}
}
