package org.qiunet.utils.test.config.conf;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.qiunet.utils.config.ConfigFileUtil;
import org.qiunet.utils.data.IKeyValueData;
import org.qiunet.utils.scanner.ClassScanner;
import org.qiunet.utils.scanner.ScannerType;

public class TestHocon {

	@BeforeClass
	public static void init() {
		ClassScanner.getInstance(ScannerType.TESTER).scanner();
	}

	@Test
	public void testConf() {
		IKeyValueData<Object, Object> configData = ConfigFileUtil.loadConfig("test.conf");
		Assert.assertEquals(8886, configData.getInt("server.node_port"));
		Assert.assertEquals(8887, configData.getInt("server.hook_port"));
		Assert.assertEquals(8888, configData.getInt("server.server_port"));

		Assert.assertEquals(8888, TestConf.getPort());
	}
}
