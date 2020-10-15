package org.qiunet.cross.actor;

import org.qiunet.flash.handler.common.player.AbstractPlayerActor;
import org.qiunet.flash.handler.common.player.event.BasePlayerEventData;
import org.qiunet.flash.handler.context.session.DSession;

/***
 * CrossPlayerActor 的父类
 *
 * @author qiunet
 * 2020-10-14 17:20
 */
public class CrossPlayerActor extends AbstractPlayerActor<CrossPlayerActor> {
	private long playerId;

	public CrossPlayerActor(DSession session) {
		super(session);
	}

	@Override
	public boolean isCrossStatus() {
		return false;
	}

	@Override
	protected <T1 extends BasePlayerEventData> void submitEvent(T1 eventData) {

	}

	@Override
	protected String getIdent() {
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
