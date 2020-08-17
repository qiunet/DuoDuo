package org.qiunet.utils.string;

import org.junit.Assert;
import org.junit.Test;
import org.qiunet.utils.base.BaseTest;
import org.qiunet.utils.secret.StrCodecUtil;

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
		Assert.assertEquals(str , StrCodecUtil.decrypt(encrypt));
	}
}
