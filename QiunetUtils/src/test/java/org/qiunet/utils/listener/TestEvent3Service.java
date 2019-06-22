package org.qiunet.utils.listener;

import org.junit.Assert;

public class TestEvent3Service {
	/***
	 * 没有实现IEventListener
	 * 不会被调用
	 * @param eventData
	 */
	@EventHandler(value = Test1EventData.class)
	public void eventHandler(IEventData eventData) {

		// 调用.必然出错
		Assert.assertEquals(0, TestListener.test1Count.incrementAndGet());
	}
}
