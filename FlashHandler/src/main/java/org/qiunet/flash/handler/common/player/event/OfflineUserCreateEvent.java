package org.qiunet.flash.handler.common.player.event;

import org.qiunet.flash.handler.common.player.offline.OfflinePlayerActor;

/***
 * 离线用户actor创建.
 *
 * @author qiunet
 * 2021/12/7 15:19
 */
public class OfflineUserCreateEvent extends BasePlayerEvent {

	public static OfflineUserCreateEvent valueOf(OfflinePlayerActor actor){
		OfflineUserCreateEvent data = new OfflineUserCreateEvent();
	    data.setPlayer(actor);
		return data;
	}

	public OfflinePlayerActor getActor() {
		return (OfflinePlayerActor) getPlayer();
	}
}
