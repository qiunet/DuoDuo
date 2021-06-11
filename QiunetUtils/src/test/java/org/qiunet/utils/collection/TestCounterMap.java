package org.qiunet.utils.collection;

import org.junit.Assert;
import org.junit.Test;

/***
 *
 * @author qiunet
 * 2020-08-30 22:07
 **/
public class TestCounterMap {

	private static final CounterMap<Integer> counterMap = new CounterMap<>();
	@Test
	public void test(){
		counterMap.increase(1);
		Assert.assertEquals(counterMap.getCount(1), 1);
		counterMap.increase(1);
		Assert.assertEquals(counterMap.getCount(1), 2);

		counterMap.increase(2, 10);
		counterMap.increase(2, 11);
		Assert.assertEquals(counterMap.getCount(2), 21);

		counterMap.decrease(2, 1);
		Assert.assertEquals(counterMap.getCount(2), 20);
	}
}
