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
	public void testClassScanner(){
		ScannerAllClassFile scannerAllClassFile = new ScannerAllClassFile();
		scannerAllClassFile.addScannerHandler(new ActionScannerHandler());
		try {
			scannerAllClassFile.scanner();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		Assert.assertNotNull(clazzName);
		if (clazzName != null) {
			Assert.assertEquals("PlayerHandler", clazzName);
		}
	}
}
