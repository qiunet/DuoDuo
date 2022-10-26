package org.qiunet.flash.handler.common.player.event;

import org.qiunet.flash.handler.common.player.offline.OfflinePlayerActor;

/***
 * 离线用户actor创建.
 *
 * @author qiunet
 * 2021/12/7 15:19
 */
public class OfflineUserCreateEvent extends UserEventData {

	private OfflinePlayerActor actor;

	public static OfflineUserCreateEvent valueOf(OfflinePlayerActor actor){
		OfflineUserCreateEvent data = new OfflineUserCreateEvent();
	    data.actor = actor;
		return data;
	}

	public OfflinePlayerActor getActor() {
		return actor;
	}
}
