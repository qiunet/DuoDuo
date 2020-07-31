package org.qiunet.utils.listener;

@EventListener(LevelUpEventData.LevelUpListener.class)
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

	public interface LevelUpListener {
		void onLevelUp(LevelUpEventData data);
	}
}
