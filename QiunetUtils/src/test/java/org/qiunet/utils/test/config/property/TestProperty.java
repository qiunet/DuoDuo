package org.qiunet.utils.test.config.property;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.qiunet.utils.config.ConfigFileUtil;
import org.qiunet.utils.data.IKeyValueData;

public class TestProperty {
	@Test
	public void testPropertyUtil(){
		IKeyValueData<Object, Object> keyValueData = ConfigFileUtil.loadConfig("db.properties");
		Assertions.assertEquals("公告测试\n内容", keyValueData.getString("content"));
	}
}
