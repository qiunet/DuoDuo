package org.qiunet.flash.handler.common.player.event;

import com.google.common.base.Preconditions;
import org.qiunet.flash.handler.common.player.AbstractUserActor;

/***
 * 鉴权通过事件
 *
 * @author qiunet
 * 2020-10-15 12:51
 */
public class LoginSuccessEvent extends UserEvent {

	public LoginSuccessEvent(AbstractUserActor player) {
		this.setPlayer(player);
	}

	@Override
	public LoginSuccessEvent setPlayer(AbstractUserActor player) {
		Preconditions.checkState(player.isAuth(), "actor need auth!");
		super.setPlayer(player);
		return this;
	}
}
