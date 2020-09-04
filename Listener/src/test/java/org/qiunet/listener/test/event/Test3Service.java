package org.qiunet.listener.test.event;

import org.junit.Assert;
import org.qiunet.listener.event.EventHandlerWeightType;
import org.qiunet.listener.event.EventListener;
public class Test3Service {

	@EventListener(EventHandlerWeightType.MIDDLE)
	public void onLogin(LoginEventData data) {
		Assert.assertEquals(data.getUid(), TestListener.uid);
		Assert.assertEquals(2, TestListener.loginEventCount.incrementAndGet());
	}
}
