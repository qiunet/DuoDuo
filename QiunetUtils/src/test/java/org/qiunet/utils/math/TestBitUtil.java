package org.qiunet.utils.math;

import org.junit.Assert;
import org.junit.Test;

/***
 *
 *
 * @author qiunet
 * 2020-04-01 17:35
 ***/
public class TestBitUtil {
	@Test
	public void testBit(){
		int ss1 = BitUtil.writeBit(0, 12, 0);
		int ss2 = BitUtil.writeBit(ss1, 142342, 4);

		Assert.assertEquals(12, BitUtil.readBit(ss2, 0, 4));
		Assert.assertEquals(142342, BitUtil.readBit(ss2, 4, 28));
	}
}
