package org.qiunet.listener.test.event;

import org.junit.Assert;
import org.qiunet.listener.event.EventHandlerWeightType;
import org.qiunet.listener.event.EventListener;

public class Test1Service {

	@EventListener(EventHandlerWeightType.HIGHEST)
	public void onLogin(LoginEventData data) {
		Assert.assertEquals(data.getUid(), TestListener.uid);
		Assert.assertEquals(1, TestListener.loginEventCount.incrementAndGet());
	}

}
