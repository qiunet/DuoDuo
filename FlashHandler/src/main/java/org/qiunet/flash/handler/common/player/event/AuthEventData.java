package org.qiunet.flash.handler.common.player.event;

import com.google.common.base.Preconditions;
import org.qiunet.flash.handler.common.player.AbstractPlayerActor;
import org.qiunet.listener.event.IEventData;

/***
 * 鉴权通过事件
 *
 * @author qiunet
 * 2020-10-15 12:51
 */
public class AuthEventData implements IEventData {

	private AbstractPlayerActor playerActor;

	public static AuthEventData valueOf(AbstractPlayerActor playerActor) {
		Preconditions.checkState(playerActor.isAuth(), "actor need auth!");
		AuthEventData eventData = new AuthEventData();
		eventData.playerActor = playerActor;
		return eventData;
	}

	public AbstractPlayerActor getPlayerActor() {
		return playerActor;
	}
}
