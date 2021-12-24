package org.qiunet.utils.test.string;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.qiunet.utils.secret.StrCodecUtil;
import org.qiunet.utils.test.base.BaseTest;

/**
 * @author qiunet
 *         Created on 16/12/30 07:20.
 */
public class TestStrCodecUtils extends BaseTest{
	@Test
	public void testCodec(){
		String str = "星河战神";
		String encrypt = StrCodecUtil.encrypt(str);
		logger.info(encrypt);
		Assertions.assertEquals(str , StrCodecUtil.decrypt(encrypt));
	}
}
