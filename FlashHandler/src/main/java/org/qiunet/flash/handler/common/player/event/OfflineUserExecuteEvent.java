package org.qiunet.flash.handler.common.player.event;

import org.qiunet.utils.listener.event.IEventData;

/***
 * 让Offline user 执行一个代码
 *
 * @author qiunet
 * 2021/12/10 19:57
 */
public class OfflineUserExecuteEvent implements IEventData {

	private long playerId;

	private Runnable runnable;

	public static OfflineUserExecuteEvent valueOf(Runnable runnable, long playerId) {
		OfflineUserExecuteEvent data = new OfflineUserExecuteEvent();
		data.playerId = playerId;
		data.runnable = runnable;
		return data;
	}

	public long getPlayerId() {
		return playerId;
	}

	public void run() {
		runnable.run();
	}
}
