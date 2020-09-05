package org.qiunet.utils.scanner;

import org.junit.Assert;
import org.junit.Test;

/**
 * @author qiunet
 *         Created on 17/1/23 19:57.
 */
public class TestClassScanner {
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
