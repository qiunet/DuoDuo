package org.qiunet.utils.test.listener;

import org.junit.jupiter.api.Assertions;
import org.qiunet.utils.listener.event.EventListener;

public class Test2Service {

	@EventListener
	public void onLevelUp(LevelUpEventData data) {
		TestListener.levelUpEventCount.incrementAndGet();
		Assertions.assertEquals(TestListener.uid, data.getUid());
		Assertions.assertEquals(TestListener.oldLevel, data.getOldLevel());
		Assertions.assertEquals(TestListener.newLevel, data.getNewLevel());
	}

	@EventListener
	private void onLogin(LoginEventData data) {
		Assertions.assertEquals(3, TestListener.loginEventCount.incrementAndGet());
	}
}
