package org.qiunet.utils.listener;

import org.junit.Assert;
import org.qiunet.utils.classScanner.Singleton;

@Singleton
public class Test3Service implements LoginEventData.LoginListener {

	@Override
	@EventHandlerWeight(EventHandlerWeightType.MIDDLE)
	public void onLogin(LoginEventData data) {
		Assert.assertEquals(data.getUid(), TestListener.uid);
		Assert.assertEquals(2, TestListener.loginEventCount.incrementAndGet());
	}
}
