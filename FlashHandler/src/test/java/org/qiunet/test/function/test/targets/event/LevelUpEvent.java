package org.qiunet.test.function.test.targets.event;

import org.qiunet.flash.handler.common.player.event.BasePlayerEvent;

/***
 *
 *
 * @author qiunet
 * 2020-11-23 17:50
 */
public class LevelUpEvent extends BasePlayerEvent {

	private int level;

	public static LevelUpEvent valueOf(int level) {
		LevelUpEvent eventData = new LevelUpEvent();
		eventData.level = level;
		return eventData;
	}

	public int getLevel() {
		return level;
	}
}
