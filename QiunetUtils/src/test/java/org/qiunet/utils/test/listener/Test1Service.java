package org.qiunet.utils.test.listener;

import org.junit.jupiter.api.Assertions;
import org.qiunet.utils.listener.event.EventHandlerWeightType;
import org.qiunet.utils.listener.event.EventListener;

public class Test1Service {

	@EventListener(EventHandlerWeightType.HIGHEST)
	public void onLogin(LoginEventData data) {
		Assertions.assertEquals(data.getUid(), TestListener.uid);
		Assertions.assertEquals(1, TestListener.loginEventCount.incrementAndGet());
	}

}
