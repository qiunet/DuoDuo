package org.qiunet.utils.test.listener;

import org.junit.jupiter.api.Assertions;
import org.qiunet.utils.listener.event.EventListener;

public class Test2Service {

	@EventListener
	public void onLevelUp(LevelUpEvent data) {
		TestListener.levelUpEventCount.incrementAndGet();
		Assertions.assertEquals(TestListener.uid, data.uid());
		Assertions.assertEquals(TestListener.oldLevel, data.oldLevel());
		Assertions.assertEquals(TestListener.newLevel, data.newLevel());
	}

	@EventListener
	private void onLogin(LoginEvent data) {
		Assertions.assertEquals(3, TestListener.loginEventCount.incrementAndGet());
	}
}
