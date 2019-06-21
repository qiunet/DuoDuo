package org.qiunet.utils.listener;

import org.junit.Assert;

public class TestEvent1Service implements IEventListener {

	@Override
	@EventHandler({
		Test1EventData.class
	})
	public void eventHandler(IEventData eventData) {
		Assert.assertEquals(Test1EventData.class,
			eventData.getClass());

		TestListener.test1Count.incrementAndGet();
	}
}
