package org.qiunet.utils.test.collection.safeCollections;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.qiunet.utils.collection.safe.SafeList;

import java.util.Iterator;

/**
 * @author qiunet
 *         Created on 17/3/1 16:47.
 */
public class TestSafeList {
	@Test
	public void testSafeList(){
		SafeList<String> list = new SafeList<>();
		list.add("a");
		list.add("b");
		list.convertToUnmodifiable();
		Assertions.assertTrue(list.contains("a"));
		boolean exception = false;
		try {
			list.add("c");
		}catch (Exception e){
			exception = true;
		}
		Assertions.assertTrue(exception);

		exception = false;
		try {
			for(Iterator<String> it = list.iterator(); it.hasNext();){
				String po = it.next();
				if("a".equals(po)){
					it.remove();
					continue;
				}

			}
		}catch (Exception e){
			exception = true;
		}
		Assertions.assertTrue(exception);

	}
}
