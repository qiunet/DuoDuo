package org.qiunet.utils.test.secret;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.qiunet.utils.secret.StrCodecUtil;

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

		Assertions.assertEquals(deStr, name);
	}
}
