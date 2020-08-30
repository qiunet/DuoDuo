package org.qiunet.utils.collection;

import org.junit.Assert;
import org.junit.Test;

/***
 *
 * @author qiunet
 * 2020-08-30 22:07
 **/
public class TestCounterMap {

	private static CounterMap<Integer> counterMap = new CounterMap<>();
	@Test
	public void test(){
		counterMap.incr(1);
		Assert.assertEquals(counterMap.getCount(1), 1);
		counterMap.incr(1);
		Assert.assertEquals(counterMap.getCount(1), 2);

		counterMap.incr(2, 10);
		counterMap.incr(2, 11);
		Assert.assertEquals(counterMap.getCount(2), 21);

		counterMap.decr(2, 1);
		Assert.assertEquals(counterMap.getCount(2), 20);
	}
}
