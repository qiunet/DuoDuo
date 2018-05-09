package org.qiunet.utils.property;

import org.junit.Assert;
import org.junit.Test;
import org.qiunet.utils.data.IKeyValueData;
import org.qiunet.utils.properties.PropertiesUtil;

public class TestProperty {
	@Test
	public void testPropertyUtil(){
		IKeyValueData keyValueData = PropertiesUtil.loadPropertiesFromResourcesPath("db.properties");
		Assert.assertEquals("公告测试\n内容", keyValueData.getString("content"));
	}
}
