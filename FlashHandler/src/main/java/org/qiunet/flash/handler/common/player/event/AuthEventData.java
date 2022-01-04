package org.qiunet.flash.handler.common.player.event;

import com.google.common.base.Preconditions;
import org.qiunet.flash.handler.common.player.AbstractUserActor;
import org.qiunet.flash.handler.common.player.IPlayer;

/***
 * 鉴权通过事件
 *
 * @author qiunet
 * 2020-10-15 12:51
 */
public class AuthEventData extends UserEventData {

	public AuthEventData(AbstractUserActor player) {
		this.setPlayer(player);
	}

	@Override
	public AuthEventData setPlayer(IPlayer player) {
		Preconditions.checkState(((AbstractUserActor) player).isAuth(), "actor need auth!");
		super.setPlayer(player);
		return this;
	}
}
