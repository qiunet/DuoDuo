package org.qiunet.utils.test.config.property;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.qiunet.utils.scanner.ClassScanner;
import org.qiunet.utils.scanner.ScannerType;

/**
 * @author qiunet
 *         Created on 17/1/5 15:35.
 */
public class TestLoaderProperties {
	@Test
	@Disabled(value = "Do it manual!")
	public void testProperties() throws Exception {
		ClassScanner.getInstance(ScannerType.FILE_CONFIG).scanner();

		String str = DbProperties.getContent();
		Assertions.assertEquals(str, "公告测试\n内容");
	}
}
