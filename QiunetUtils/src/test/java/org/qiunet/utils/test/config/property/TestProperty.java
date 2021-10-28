package org.qiunet.utils.test.config.property;

import org.junit.Assert;
import org.junit.Test;
import org.qiunet.utils.config.ConfigFileUtil;
import org.qiunet.utils.data.IKeyValueData;

public class TestProperty {
	@Test
	public void testPropertyUtil(){
		IKeyValueData<Object, Object> keyValueData = ConfigFileUtil.loadConfig("db.properties");
		Assert.assertEquals("公告测试\n内容", keyValueData.getString("content"));
	}
}
