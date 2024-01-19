package org.qiunet.test.function.test.targets.event;

import org.qiunet.flash.handler.common.player.event.PlayerEvent;

/***
 *
 *
 * @author qiunet
 * 2020-11-23 17:50
 */
public class KillBossEvent extends PlayerEvent {

	private int bossId;

	public static KillBossEvent valueOf(int bossId) {
		KillBossEvent eventData = new KillBossEvent();
		eventData.bossId = bossId;
		return eventData;
	}

	public int getBossId() {
		return bossId;
	}
}
