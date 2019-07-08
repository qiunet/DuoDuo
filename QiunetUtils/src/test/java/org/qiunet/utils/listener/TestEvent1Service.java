package org.qiunet.utils.listener;

import org.junit.Assert;

public class TestEvent1Service implements IEventListener {

	@Override
	@EventHandler(value = Test1EventData.class, weight = 10)
	public void eventHandler(IEventData eventData) {
		Assert.assertEquals(Test1EventData.class,
			eventData.getClass());

		Assert.assertEquals(1, TestListener.test1Count.incrementAndGet());
	}
}
