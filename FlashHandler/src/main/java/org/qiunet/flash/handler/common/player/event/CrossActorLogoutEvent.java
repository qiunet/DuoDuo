package org.qiunet.flash.handler.common.player.event;

import org.qiunet.cross.event.CrossPlayerEvent;
import org.qiunet.flash.handler.netty.server.constants.CloseCause;

/***
 * 登出事件
 *
 * @author qiunet
 * 2020-10-15 12:51
 */
public class CrossActorLogoutEvent extends CrossPlayerEvent {
	private final CloseCause cause;

	public CrossActorLogoutEvent(CloseCause cause) {
		this.cause = cause;
	}

	public CloseCause getCause() {
		return cause;
	}
}
