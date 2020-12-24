package org.qiunet.utils.config.hocon;

import com.typesafe.config.Config;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.qiunet.utils.scanner.ClassScanner;
import org.qiunet.utils.scanner.ScannerType;

public class TestHocon {

	@BeforeClass
	public static void init() {
		ClassScanner.getInstance(ScannerType.TESTER).scanner();
	}

	@Test
	public void testConf() {
		Config config = HoconUtil.loadConf("test.conf");
		Assert.assertEquals(8886, config.getInt("server.node_port"));
		Assert.assertEquals(8887, config.getInt("server.hook_port"));
		Assert.assertEquals(8888, config.getInt("server.server_port"));

		Assert.assertEquals(8888, TestConf.getPort());
	}
}
