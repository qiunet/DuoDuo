package org.qiunet.cross.actor;

import com.google.common.base.Preconditions;
import org.qiunet.cross.actor.message.Cross2PlayerResponse;
import org.qiunet.cross.event.BaseCrossPlayerEventData;
import org.qiunet.cross.event.CrossEventManager;
import org.qiunet.flash.handler.common.player.AbstractUserActor;
import org.qiunet.flash.handler.common.player.event.AuthEventData;
import org.qiunet.flash.handler.common.player.event.BasePlayerEventData;
import org.qiunet.flash.handler.context.request.data.pb.IpbResponseData;
import org.qiunet.flash.handler.context.session.DSession;
import org.qiunet.listener.event.EventManager;

/***
 * 跨服服务的playerActor
 *
 * @author qiunet
 * 2020-10-14 17:20
 */
public class CrossPlayerActor extends AbstractUserActor<CrossPlayerActor> {
	private long playerId;

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

	@Override
	public String getIdent() {
		return "CrossPlayerActor_"+playerId;
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
}
