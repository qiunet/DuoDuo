package org.qiunet.listener.test.event;

import org.qiunet.listener.event.IEventData;

public class LevelUpEventData implements IEventData {
	private long uid;
	private int oldLevel;
	private int newLevel;

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
