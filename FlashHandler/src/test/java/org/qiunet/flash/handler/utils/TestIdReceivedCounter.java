package org.qiunet.flash.handler.utils;

import org.junit.Assert;
import org.junit.Test;
import org.qiunet.flash.handler.common.utils.IdReceivedCounter;

/***
 *
 * @Author qiunet
 * @Date Create in 2018/10/19 17:36
 **/
public class TestIdReceivedCounter {

	@Test
	public void testCounter() {
		IdReceivedCounter counter = new IdReceivedCounter();
		Assert.assertTrue(counter.canReceive(1));
		Assert.assertFalse(counter.canReceive(3 * IdReceivedCounter.BIT_COUNT + 10));
		Assert.assertTrue(counter.canReceive(IdReceivedCounter.BIT_COUNT + 1));
		Assert.assertTrue(counter.getPageCount() == 1);
		Assert.assertTrue(counter.canReceive(2*IdReceivedCounter.BIT_COUNT + 1));
		Assert.assertTrue(counter.getPageCount() == 2);
		Assert.assertFalse(counter.canReceive(1));
	}

	/***
	 *
	 */
	@Test
	public void testLoop() {
		IdReceivedCounter counter = new IdReceivedCounter();
		for (int i = 1; i <= 200; i++) {
			long a0 = counter.getPageCounter0();
			long a1 = counter.getPageCounter1();
			int page = counter.getPageCount();

			Assert.assertTrue(counter.canReceive(i));
			Assert.assertTrue(counter.getPageCount() == Math.max(1, (i-1) / IdReceivedCounter.BIT_COUNT));
			if (i > (page+1) * IdReceivedCounter.BIT_COUNT){
				a0 = a1;
				a1 = 0;
			}

			long addCount = ((long)1 << ((i - 1) % IdReceivedCounter.BIT_COUNT));
			if ((i-1) / IdReceivedCounter.BIT_COUNT < counter.getPageCount()){
				// 留在0
				Assert.assertEquals(counter.getPageCounter0(), a0 + addCount);
				Assert.assertEquals(counter.getPageCounter1(), a1);
			}else {
				// 落1上
				Assert.assertEquals(counter.getPageCounter0(), a0);
				Assert.assertEquals(counter.getPageCounter1(), a1 + addCount);
			}
			Assert.assertFalse(counter.canReceive(i));
		}
	}
}
