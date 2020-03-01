package org.qiunet.utils.listener;

import org.junit.Assert;

public class Test3Service implements LoginEventData.LoginListener {

	@Override
	@EventHandlerWeight(EventHandlerWeightType.MIDDLE)
	public void onLogin(LoginEventData data) {
		Assert.assertEquals(data.getUid(), TestListener.uid);
		Assert.assertEquals(2, TestListener.loginEventCount.incrementAndGet());
	}
}
