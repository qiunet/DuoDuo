package org.qiunet.flash.handler.common.player.event;

/***
 * 跨服玩家登出事件
 *
 * @author qiunet
 * 2021/11/26 09:38
 */
public class CrossPlayerLogoutEvent extends BasePlayerEvent {
	/**
	 * 退出的serverId
	 */
	private int serverId;

	public static CrossPlayerLogoutEvent valueOf(int serverId){
		CrossPlayerLogoutEvent data = new CrossPlayerLogoutEvent();
		data.serverId = serverId;
		return data;
	}

	public int getServerId() {
		return serverId;
	}
}
