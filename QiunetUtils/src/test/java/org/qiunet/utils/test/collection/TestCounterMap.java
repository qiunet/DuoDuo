package org.qiunet.utils.test.collection;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.qiunet.utils.collection.CounterMap;

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
		Assertions.assertEquals(counterMap.getCount(1), 1);
		counterMap.increase(1);
		Assertions.assertEquals(counterMap.getCount(1), 2);

		counterMap.increase(2, 10);
		counterMap.increase(2, 11);
		Assertions.assertEquals(counterMap.getCount(2), 21);

		counterMap.decrease(2, 1);
		Assertions.assertEquals(counterMap.getCount(2), 20);
	}
}
