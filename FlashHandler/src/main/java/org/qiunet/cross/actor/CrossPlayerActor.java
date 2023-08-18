package org.qiunet.cross.actor;

import com.google.common.base.Preconditions;
import org.qiunet.cross.event.BaseCrossPlayerEvent;
import org.qiunet.cross.event.CrossEventRequest;
import org.qiunet.flash.handler.common.player.AbstractUserActor;
import org.qiunet.flash.handler.common.player.IPlayerFireEvent;
import org.qiunet.flash.handler.common.player.event.BasePlayerEvent;
import org.qiunet.flash.handler.common.player.event.CrossActorLogoutEvent;
import org.qiunet.flash.handler.common.player.event.LoginSuccessEvent;
import org.qiunet.flash.handler.context.session.ISession;
import org.qiunet.utils.listener.event.EventManager;

/***
 * 跨服服务的playerActor
 *
 * @author qiunet
 * 2020-10-14 17:20
 */
public final class CrossPlayerActor extends AbstractUserActor<CrossPlayerActor>
		implements IPlayerFireEvent<BaseCrossPlayerEvent, BasePlayerEvent, CrossPlayerActor> {
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
			EventManager.post(new CrossActorLogoutEvent(cause).setPlayer(this), (mtd, ex) -> {
				logger.error("PlayerActor: "+ session +" session close error in method ["+mtd.getName()+"]!", ex);
			});
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

	@Override
	public ISession getKcpSession() {
		// 并非真是依靠kcpSession发送.
		return session;
	}
}
