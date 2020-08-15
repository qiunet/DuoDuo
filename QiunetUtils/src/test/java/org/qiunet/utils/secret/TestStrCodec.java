package org.qiunet.utils.secret;

import org.junit.Assert;
import org.junit.Test;

/**
 * Created by qiunet.
 * 17/10/11
 */
public class TestStrCodec {
	@Test
	public void testStrCodec(){
		String name = "多多少少";

		String str = StrCodecUtil.encrypt(name);

		String deStr = StrCodecUtil.decrypt(str);

		Assert.assertEquals(deStr, name);
	}
}
