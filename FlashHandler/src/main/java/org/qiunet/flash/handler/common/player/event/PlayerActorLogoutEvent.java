package org.qiunet.flash.handler.common.player.event;

import org.qiunet.flash.handler.common.player.PlayerActor;
import org.qiunet.flash.handler.netty.server.constants.CloseCause;

/***
 * 登出事件
 *
 * @author qiunet
 * 2020-10-15 12:51
 */
public class PlayerActorLogoutEvent extends UserEventData {
	private final CloseCause cause;

	public PlayerActorLogoutEvent(CloseCause cause) {
		this.cause = cause;
	}

	@Override
	public PlayerActor getPlayer() {
		return super.getPlayer();
	}

	public CloseCause getCause() {
		return cause;
	}
}
