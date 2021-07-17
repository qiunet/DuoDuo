package org.qiunet.utils.test.config.property;

import org.junit.Assert;
import org.junit.Test;
import org.qiunet.utils.scanner.ClassScanner;

/**
 * @author qiunet
 *         Created on 17/1/5 15:35.
 */
public class TestLoaderProperties {
	@Test
	public void testProperties() throws Exception {
		ClassScanner.getInstance().scanner();

		String str = DbProperties.getContent();
		Assert.assertEquals(str, "公告测试\n内容");
	}
}
