package org.qiunet.utils.test.collection;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.qiunet.utils.collection.DuMap;

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

		Assertions.assertEquals(duMap.size(), 2);
		Assertions.assertTrue(duMap.containsKey(1));
		Assertions.assertTrue(duMap.containsKey(2));
		Assertions.assertTrue(duMap.containsVal("1"));
		Assertions.assertTrue(duMap.containsVal("2"));


		Assertions.assertEquals(duMap.getKey("1"), Integer.valueOf(1));
		Assertions.assertEquals(duMap.getVal(2), "2");
	}
}
