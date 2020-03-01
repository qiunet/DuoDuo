package org.qiunet.utils.listener;

import org.junit.Assert;
import org.qiunet.utils.common.CommonUtil;

public class TestEvent1And2Service implements IEventListener {

	@Override
	// 两个相同的EventHandler 会把权重相加
	@EventHandler(value = Test1EventData.class, weight = 2)
	@EventHandler(value = Test1EventData.class, weight = 1)
	@EventHandler(Test2EventData.class)
	public void eventHandler(IEventData eventData) {
		Assert.assertTrue(CommonUtil.existInList(eventData.getClass(), Test1EventData.class,
			Test2EventData.class));
		if (eventData.getClass() == Test1EventData.class)
			Assert.assertEquals(2, TestListener.test1Count.incrementAndGet());
		else if (eventData.getClass() == Test2EventData.class)
			TestListener.test2Count.incrementAndGet();
	}
}
