package org.qiunet.flash.handler.common.player.event;

import org.qiunet.flash.handler.common.player.offline.enums.OfflinePlayerDestroyCause;

/***
 * 离线用户actor 销毁
 *
 * @author qiunet
 * 2021/12/7 15:19
 */
public class OfflineUserDestroyEvent extends PlayerEvent {
	/**
	 * 原因
	 */
	private OfflinePlayerDestroyCause cause;

	public static OfflineUserDestroyEvent valueOf(OfflinePlayerDestroyCause cause){
		OfflineUserDestroyEvent data = new OfflineUserDestroyEvent();
		data.cause = cause;
		return data;
	}

	public OfflinePlayerDestroyCause getCause() {
		return cause;
	}
}
