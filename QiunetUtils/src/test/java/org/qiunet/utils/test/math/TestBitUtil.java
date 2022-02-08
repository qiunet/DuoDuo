package org.qiunet.utils.test.math;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.qiunet.utils.math.BitUtil;

/***
 *
 *
 * @author qiunet
 * 2020-04-01 17:35
 ***/
public class TestBitUtil {
	@Test
	public void testBit(){
		int ss1 = BitUtil.writeBit(0, 13, 0); // 13的二进制 1101
		int ss2 = BitUtil.writeBit(ss1, 142342, 4);

		Assertions.assertEquals(13, BitUtil.readBit(ss2, 0, 4));
		Assertions.assertEquals(142342, BitUtil.readBit(ss2, 4, 28));

		int ss3 = BitUtil.setBit(0, 12);
		Assertions.assertTrue(BitUtil.isBitSet(ss3, 12));

		int ss4 = BitUtil.setBit(ss3, 1);
		int ss5 = BitUtil.setBit(ss4, 2);

		int ss6 = BitUtil.removeBit(ss5, 12);
		Assertions.assertEquals(6, ss6);
		Assertions.assertFalse(BitUtil.isBitSet(ss6, 12));
	}
}
