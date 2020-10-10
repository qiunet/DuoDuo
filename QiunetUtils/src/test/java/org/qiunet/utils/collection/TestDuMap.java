package org.qiunet.utils.collection;

import org.junit.Assert;
import org.junit.Test;

/***
 *
 * @author qiunet
 * 2020-08-25 08:28
 **/
public class TestDuMap {
	@Test
	public void test(){
		DuMap<Integer, String> duMap = new DuMap<>();
		duMap.put(1, "1").put(2, "2");

		Assert.assertEquals(duMap.size(), 2);
		Assert.assertTrue(duMap.containsKey(1));
		Assert.assertTrue(duMap.containsKey(2));
		Assert.assertTrue(duMap.containsVal("1"));
		Assert.assertTrue(duMap.containsVal("2"));


		Assert.assertEquals(duMap.getKey("1"), Integer.valueOf(1));
		Assert.assertEquals(duMap.getVal(2), "2");
	}
}
