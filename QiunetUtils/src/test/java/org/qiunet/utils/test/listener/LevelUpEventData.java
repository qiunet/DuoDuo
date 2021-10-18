package org.qiunet.utils.test.listener;

import org.qiunet.utils.listener.event.IEventData;

public class LevelUpEventData implements IEventData {
	private final long uid;
	private final int oldLevel;
	private final int newLevel;

	public LevelUpEventData(long uid, int oldLevel, int newLevel) {
		this.uid = uid;
		this.oldLevel = oldLevel;
		this.newLevel = newLevel;
	}

	public long getUid() {
		return uid;
	}

	public int getOldLevel() {
		return oldLevel;
	}

	public int getNewLevel() {
		return newLevel;
	}

}
