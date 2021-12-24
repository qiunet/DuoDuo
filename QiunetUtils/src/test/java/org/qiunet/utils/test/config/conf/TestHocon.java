package org.qiunet.utils.test.config.conf;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.qiunet.utils.config.ConfigFileUtil;
import org.qiunet.utils.data.IKeyValueData;
import org.qiunet.utils.scanner.ClassScanner;
import org.qiunet.utils.scanner.ScannerType;

public class TestHocon {

	@BeforeAll
	public static void init() {
		ClassScanner.getInstance(ScannerType.FILE_CONFIG).scanner();
	}

	@Test
	public void testConf() {
		IKeyValueData<Object, Object> configData = ConfigFileUtil.loadConfig("test.conf");
		Assertions.assertEquals(8886, configData.getInt("server.node_port"));
		Assertions.assertEquals(8887, configData.getInt("server.hook_port"));
		Assertions.assertEquals(8888, configData.getInt("server.server_port"));

		Assertions.assertEquals(8888, TestConf.getPort());
	}
}
