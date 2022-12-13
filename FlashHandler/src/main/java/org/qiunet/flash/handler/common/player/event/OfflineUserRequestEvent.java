package org.qiunet.flash.handler.common.player.event;

import org.qiunet.utils.listener.event.IListenerEvent;

/***
 * 不在线玩家的跨服请求事件
 * @author qiunet
 * 2021/12/8 14:57
 */
public class OfflineUserRequestEvent implements IListenerEvent {

	private UserEvent eventData;

	private long playerId;

	public static OfflineUserRequestEvent valueOf(UserEvent eventData, long playerId) {
		OfflineUserRequestEvent data = new OfflineUserRequestEvent();
		data.eventData = eventData;
		data.playerId = playerId;
		return data;
	}

	public UserEvent getEventData() {
		return eventData;
	}

	public long getPlayerId() {
		return playerId;
	}
}
