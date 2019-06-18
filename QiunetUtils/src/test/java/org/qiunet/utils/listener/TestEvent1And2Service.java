package org.qiunet.utils.listener;

import org.junit.Assert;
import org.qiunet.utils.common.CommonUtil;

public class TestEvent1And2Service implements IEventListener {

	@Override
	@EventHandler({
		Test1EventData.class,
		Test2EventData.class
	})
	public void eventHandler(IEventData eventData) {
		Assert.assertTrue(CommonUtil.existInList(eventData.getClass(), new Class[]{Test1EventData.class,
			Test2EventData.class}));
	}
}
