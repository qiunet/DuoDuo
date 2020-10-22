package org.qiunet.flash.handler.common.player.event;

import com.google.common.base.Preconditions;
import org.qiunet.flash.handler.common.player.AbstractUserActor;

/***
 * 鉴权通过事件
 *
 * @author qiunet
 * 2020-10-15 12:51
 */
public class AuthEventData<T extends AbstractUserActor<T>> extends BaseUserEventData<T> {

	public static AuthEventData valueOf() {
		return new AuthEventData();
	}

	@Override
	public void setPlayer(T player) {
		Preconditions.checkState(player.isAuth(), "actor need auth!");
		super.setPlayer(player);
	}
}
