package org.qiunet.utils.listener;

import org.junit.Assert;

public class Test1Service implements LoginEventData.LoginListener {

	@Override
	@EventHandlerWeight(EventHandlerWeightType.HIGHEST)
	public void onLogin(LoginEventData data) {
		Assert.assertEquals(data.getUid(), TestListener.uid);
		Assert.assertEquals(1, TestListener.loginEventCount.incrementAndGet());
	}

}
