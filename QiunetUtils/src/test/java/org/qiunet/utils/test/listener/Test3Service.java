package org.qiunet.utils.test.listener;

import org.junit.Assert;
import org.qiunet.utils.listener.event.EventHandlerWeightType;
import org.qiunet.utils.listener.event.EventListener;
public class Test3Service {

	@EventListener(EventHandlerWeightType.MIDDLE)
	public void onLogin(LoginEventData data) {
		Assert.assertEquals(data.getUid(), TestListener.uid);
		Assert.assertEquals(2, TestListener.loginEventCount.incrementAndGet());
	}
}
