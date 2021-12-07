package org.qiunet.flash.handler.common.player.event;

import org.qiunet.utils.listener.event.IEventData;

/***
 * 离线用户actor 销毁
 *
 * @author qiunet
 * 2021/12/7 15:19
 */
public class OfflineUserDestroyEvent implements IEventData {
	/**
     * 玩家id
	 */
	private long playerId;

	public static OfflineUserDestroyEvent valueOf(long playerId){
		OfflineUserDestroyEvent data = new OfflineUserDestroyEvent();
		data.playerId = playerId;
		return data;
	}

	public long getPlayerId() {
		return playerId;
	}
}
