package org.qiunet.flash.handler.common.player.event;

import org.qiunet.flash.handler.common.player.AbstractUserActor;
import org.qiunet.flash.handler.netty.server.constants.CloseCause;

/***
 * 登出事件
 *
 * @author qiunet
 * 2020-10-15 12:51
 */
public class UserLogoutEventData<T extends AbstractUserActor<T>> extends UserEventData {
	private final CloseCause cause;

	public UserLogoutEventData(T player, CloseCause cause) {
		this.setPlayer(player);
		this.cause = cause;
	}

	public CloseCause getCause() {
		return cause;
	}
}
