package org.qiunet.utils.test.data;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
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
		Assertions.assertEquals("qiunet", commonData.getKey());
		Assertions.assertTrue(11 == commonData.getVal());
	}
}
