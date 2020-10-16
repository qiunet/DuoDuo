package org.qiunet.flash.handler.common.player.event;

import com.google.common.base.Preconditions;
import org.qiunet.flash.handler.common.player.AbstractUserActor;
import org.qiunet.listener.event.IEventData;

/***
 * 鉴权通过事件
 *
 * @author qiunet
 * 2020-10-15 12:51
 */
public class AuthEventData implements IEventData {

	private AbstractUserActor userActor;

	public static AuthEventData valueOf(AbstractUserActor userActor) {
		Preconditions.checkState(userActor.isAuth(), "actor need auth!");
		AuthEventData eventData = new AuthEventData();
		eventData.userActor = userActor;
		return eventData;
	}

	public AbstractUserActor getUserActor() {
		return userActor;
	}
}
