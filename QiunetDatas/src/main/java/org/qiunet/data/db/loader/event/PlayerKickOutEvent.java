package org.qiunet.data.db.loader.event;

import org.qiunet.utils.listener.event.IListenerEvent;

/***
 * 玩家踢出事件
 *
 * @author qiunet
 * 2021/11/25 14:34
 */
public class PlayerKickOutEvent implements IListenerEvent {
	/**
	 * 玩家ID
	 */
	private long playerId;

	public static PlayerKickOutEvent valueOf(long playerId){
		PlayerKickOutEvent data = new PlayerKickOutEvent();
		data.playerId = playerId;
		return data;
	}

	public long getPlayerId() {
		return playerId;
	}
}
