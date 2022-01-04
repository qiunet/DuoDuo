package org.qiunet.flash.handler.common.player.event;

/***
 * 离线用户actor创建.
 *
 * @author qiunet
 * 2021/12/7 15:19
 */
public class OfflineUserCreateEvent extends UserEventData {


	public static OfflineUserCreateEvent valueOf(){
		OfflineUserCreateEvent data = new OfflineUserCreateEvent();
		return data;
	}
}
