package org.qiunet.utils.test.listener;

import org.junit.jupiter.api.Assertions;
import org.qiunet.utils.listener.event.EventHandlerWeightType;
import org.qiunet.utils.listener.event.EventListener;
public class Test3Service {

	@EventListener(EventHandlerWeightType.MIDDLE)
	public void onLogin(LoginEvent data) {
		Assertions.assertEquals(data.uid(), TestListener.uid);
		Assertions.assertEquals(2, TestListener.loginEventCount.incrementAndGet());
	}
}
