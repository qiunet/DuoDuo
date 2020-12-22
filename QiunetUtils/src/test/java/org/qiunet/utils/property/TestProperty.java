package org.qiunet.utils.property;

import org.junit.Assert;
import org.junit.Test;
import org.qiunet.utils.config.properties.PropertiesUtil;
import org.qiunet.utils.data.IKeyValueData;

public class TestProperty {
	@Test
	public void testPropertyUtil(){
		IKeyValueData<Object, Object> keyValueData = PropertiesUtil.loadProperties("db.properties");
		Assert.assertEquals("公告测试\n内容", keyValueData.getString("content"));
	}
}
