package org.qiunet.utils.string;

import org.qiunet.utils.base.BaseTest;
import org.junit.Assert;
import org.junit.Test;

/**
 * @author qiunet
 *         Created on 16/11/6 13:55.
 */
public class TestStringUtil extends BaseTest{
	@Test
	public void testSplit(){
		String str = "1,2,";
		String strs [] = StringUtil.split(str, ",");
		Assert.assertEquals("",strs[2]);

		str = ",1,2";
		strs  = StringUtil.split(str, ",");
		Assert.assertTrue(strs.length == 3);
		Assert.assertEquals("", strs[0]);

		str = "1,2";
		strs  = StringUtil.split(str, ",");
		Assert.assertTrue(strs.length == 2);

		str = ",1,2,";
		strs  = StringUtil.split(str, ",");
		Assert.assertTrue(strs.length == 4);
	}
}
