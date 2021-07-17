package org.qiunet.utils.test.data;

import org.junit.Assert;
import org.junit.Test;
import org.qiunet.utils.data.CommonData;
import org.qiunet.utils.test.base.BaseTest;

/**
 * @author qiunet
 *         Created on 16/11/4 19:04.
 */
public class TestCommonData extends BaseTest {

	@Test
	public void exec() {

		CommonData<String, Integer> commonData = new CommonData("qiunet", 11);
		Assert.assertEquals("qiunet", commonData.getKey());
		Assert.assertTrue(11 == commonData.getVal());
	}
}
