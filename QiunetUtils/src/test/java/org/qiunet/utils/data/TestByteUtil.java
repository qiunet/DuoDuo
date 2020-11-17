package org.qiunet.utils.data;

import org.junit.Assert;
import org.junit.Test;


/***
 *
 *
 * @author qiunet
 * 2020-11-17 12:50
 */
public class TestByteUtil {
	@Test
	public void test(){
		byte[] bytes = ByteUtil.int2Bytes(666666);
		Assert.assertArrayEquals(bytes, new byte[]{0, 10, 44, 42});
		Assert.assertEquals(666666, ByteUtil.read32bit(bytes, 0));
	}
}
