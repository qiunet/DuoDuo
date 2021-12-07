package org.qiunet.flash.handler.common.player.event;

import org.qiunet.flash.handler.common.player.offline.OfflinePlayerActor;
import org.qiunet.utils.listener.event.IEventData;

/***
 * 离线用户actor创建.
 *
 * @author qiunet
 * 2021/12/7 15:19
 */
public class OfflineUserCreateEvent implements IEventData {
	/**
	 * 离线玩家
	 */
	private OfflinePlayerActor player;

	public static OfflineUserCreateEvent valueOf(OfflinePlayerActor player){
		OfflineUserCreateEvent data = new OfflineUserCreateEvent();
		data.player = player;
		return data;
	}

	public OfflinePlayerActor getPlayer() {
		return player;
	}
}
