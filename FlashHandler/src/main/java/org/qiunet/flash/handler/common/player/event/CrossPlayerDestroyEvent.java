package org.qiunet.flash.handler.common.player.event;


/***
 * 跨服玩家登出事件
 *
 * @author qiunet
 * 2021/11/26 09:38
 */
public class CrossPlayerDestroyEvent extends ToPlayerEvent {
	/**
	 * 退出的serverId
	 */
	private int serverId;

	public static CrossPlayerDestroyEvent valueOf(int serverId){
		CrossPlayerDestroyEvent data = new CrossPlayerDestroyEvent();
		data.serverId = serverId;
		return data;
	}

	public int getServerId() {
		return serverId;
	}
}
