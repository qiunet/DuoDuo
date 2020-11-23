package org.qiunet.function.test.targets.event;

import org.qiunet.flash.handler.common.player.event.BasePlayerEventData;
import org.qiunet.function.test.targets.PlayerActor;

/***
 *
 *
 * @author qiunet
 * 2020-11-23 17:50
 */
public class LevelUpEventData extends BasePlayerEventData<PlayerActor> {

	private int level;

	public static LevelUpEventData valueOf(int level) {
		LevelUpEventData eventData = new LevelUpEventData();
		eventData.level = level;
		return eventData;
	}

	public int getLevel() {
		return level;
	}
}
