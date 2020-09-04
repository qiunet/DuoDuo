package org.qiunet.listener.test.event;

import org.junit.Assert;
import org.qiunet.listener.event.EventListener;

public class Test2Service {

	@EventListener
	public void onLevelUp(LevelUpEventData data) {
		TestListener.levelUpEventCount.incrementAndGet();
		Assert.assertEquals(TestListener.uid, data.getUid());
		Assert.assertEquals(TestListener.oldLevel, data.getOldLevel());
		Assert.assertEquals(TestListener.newLevel, data.getNewLevel());
	}

	@EventListener
	public void onLogin(LoginEventData data) {
		Assert.assertEquals(3, TestListener.loginEventCount.incrementAndGet());
	}
}
