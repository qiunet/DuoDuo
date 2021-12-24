package org.qiunet.utils.test.scanner;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.qiunet.utils.scanner.ClassScanner;
import org.qiunet.utils.scanner.ScannerType;
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
		ClassScanner.getInstance(ScannerType.NONE, ScannerType.AUTO_WIRE)
			.addParam(ArgKey.Test, 10)
			.scanner();

		Assertions.assertNotNull(clazzName);
		Assertions.assertEquals("PlayerHandler", clazzName);
		Assertions.assertEquals(12345678, testInterface.getType());
	}
}
