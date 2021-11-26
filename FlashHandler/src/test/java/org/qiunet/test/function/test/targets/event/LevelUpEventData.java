package org.qiunet.test.function.test.targets.event;

import org.qiunet.flash.handler.common.player.event.BasePlayerEventData;

/***
 *
 *
 * @author qiunet
 * 2020-11-23 17:50
 */
public class LevelUpEventData extends BasePlayerEventData {

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
