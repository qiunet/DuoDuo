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
	 * 原因
	 */
	private CloseCause cause;

	public static CrossPlayerLogoutEvent valueOf(CloseCause cause){
		CrossPlayerLogoutEvent data = new CrossPlayerLogoutEvent();
		data.cause = cause;
		return data;
	}

	public CloseCause getCause() {
		return cause;
	}
}
