package org.qiunet.utils.listener;

import org.junit.Assert;
import org.qiunet.utils.classScanner.Singleton;

@Singleton
public class Test2Service implements LevelUpEventData.LevelUpListener,  LoginEventData.LoginListener {

	@Override
	public void onLevelUp(LevelUpEventData data) {
		TestListener.levelUpEventCount.incrementAndGet();
		Assert.assertEquals(TestListener.uid, data.getUid());
		Assert.assertEquals(TestListener.oldLevel, data.getOldLevel());
		Assert.assertEquals(TestListener.newLevel, data.getNewLevel());
	}

	@Override
	public void onLogin(LoginEventData data) {
		Assert.assertEquals(3, TestListener.loginEventCount.incrementAndGet());
	}
}
