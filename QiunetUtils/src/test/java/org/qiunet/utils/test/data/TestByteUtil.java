package org.qiunet.utils.test.data;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.qiunet.utils.data.ByteUtil;


/***
 *
 *
 * @author qiunet
 * 2020-11-17 12:50
 */
public class TestByteUtil {
	private static final int num = 666666;
	private static final byte [] arr = new byte[]{0, 10, 44, 42};
	@Test
	public void test(){
		byte[] bytes = ByteUtil.int2Bytes(num);
		Assertions.assertArrayEquals(bytes, arr);

		Assertions.assertEquals(num, ByteUtil.read32bit(arr, 0));
	}
}
