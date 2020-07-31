package org.qiunet.utils.classScanner;

import org.junit.Assert;
import org.junit.Test;
import org.qiunet.utils.base.BaseTest;

/**
 * @author qiunet
 *         Created on 17/1/23 19:57.
 */
public class TestClassScanner extends BaseTest {
	public  static String clazzName;
	@Test
	public void testClassScanner() throws Exception {
		ClassScanner.getInstance().scanner();
		Assert.assertNotNull(clazzName);
		if (clazzName != null) {
			Assert.assertEquals("PlayerHandler", clazzName);
		}
	}
}
