package org.qiunet.flash.handler.common.player.event;

/***
 * 离线用户actor 销毁
 *
 * @author qiunet
 * 2021/12/7 15:19
 */
public class OfflineUserDestroyEvent extends BasePlayerEvent {

	public static OfflineUserDestroyEvent valueOf(){
		OfflineUserDestroyEvent data = new OfflineUserDestroyEvent();
		return data;
	}
}
