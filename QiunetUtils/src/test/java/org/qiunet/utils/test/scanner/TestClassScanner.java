package org.qiunet.utils.test.scanner;

import org.junit.Assert;
import org.junit.Test;
import org.qiunet.utils.scanner.ClassScanner;
import org.qiunet.utils.scanner.anno.AutoWired;

/**
 * @author qiunet
 *         Created on 17/1/23 19:57.
 */
public class TestClassScanner {
	public  static String clazzName;
	@AutoWired
	private static ITestInterface testInterface;
	@AutoWired
	private static ITestIgnoreWired wired;

	@Test
	public void testClassScanner() {
		ClassScanner.getInstance()
			.addParam(ArgKey.Test, 10)
			.scanner();

		Assert.assertNotNull(clazzName);
		Assert.assertEquals("PlayerHandler", clazzName);
		Assert.assertEquals(12345678, testInterface.getType());
	}
}
