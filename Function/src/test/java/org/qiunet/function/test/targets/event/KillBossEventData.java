package org.qiunet.function.test.targets.event;

import org.qiunet.flash.handler.common.player.event.BasePlayerEventData;
import org.qiunet.function.test.targets.PlayerActor;

/***
 *
 *
 * @author qiunet
 * 2020-11-23 17:50
 */
public class KillBossEventData extends BasePlayerEventData<PlayerActor> {

	private int bossId;

	public static KillBossEventData valueOf(int bossId) {
		KillBossEventData eventData = new KillBossEventData();
		eventData.bossId = bossId;
		return eventData;
	}

	public int getBossId() {
		return bossId;
	}
}
