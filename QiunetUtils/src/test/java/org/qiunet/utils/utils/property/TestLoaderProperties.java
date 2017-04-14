package org.qiunet.utils.utils.property;

import org.junit.Assert;
import org.junit.Test;

/**
 * @author qiunet
 *         Created on 17/1/5 15:35.
 */
public class TestLoaderProperties {
	@Test
	public void testProperties(){
		String str = DbProperties.getInstance().getString("content");
		Assert.assertEquals(str, "公告测试\n内容");
	}
}
