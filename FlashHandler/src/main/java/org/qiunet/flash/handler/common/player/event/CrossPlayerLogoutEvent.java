package org.qiunet.flash.handler.common.player.event;

import org.qiunet.flash.handler.netty.server.constants.CloseCause;

/***
 * 跨服玩家登出事件
 *
 * @author qiunet
 * 2021/11/26 09:38
 */
public class CrossPlayerLogoutEvent extends BasePlayerEventData {
	/**
	 * 退出的serverId
	 */
	private int serverId;
	/**
	 * 原因
	 */
	private CloseCause cause;

	public static CrossPlayerLogoutEvent valueOf(CloseCause cause, int serverId){
		CrossPlayerLogoutEvent data = new CrossPlayerLogoutEvent();
		data.serverId = serverId;
		data.cause = cause;
		return data;
	}

	public int getServerId() {
		return serverId;
	}

	public CloseCause getCause() {
		return cause;
	}
}
