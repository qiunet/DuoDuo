package org.qiunet.flash.handler.common.player.event;

import org.qiunet.cross.actor.CrossPlayerActor;
import org.qiunet.cross.event.BaseCrossPlayerEvent;
import org.qiunet.flash.handler.netty.server.constants.CloseCause;

/***
 * 登出事件
 *
 * @author qiunet
 * 2020-10-15 12:51
 */
public class CrossActorLogoutEvent extends BaseCrossPlayerEvent {
	private final CloseCause cause;

	public CrossActorLogoutEvent(CloseCause cause) {
		this.cause = cause;
	}

	@Override
	public CrossPlayerActor getPlayer() {
		return super.getPlayer();
	}

	public CloseCause getCause() {
		return cause;
	}
}
