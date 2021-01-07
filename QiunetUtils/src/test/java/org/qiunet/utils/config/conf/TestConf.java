package org.qiunet.utils.config.conf;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.qiunet.utils.classScanner.ClassScanner;
import org.qiunet.utils.config.anno.DConfig;
import org.qiunet.utils.config.anno.DConfigValue;

/***
 * 注解的方式使用(scanner 扫描器扫描注解)
 * @author qiunet
 * 2020-12-24 20:59
 */
@DConfig("test.conf")
public class TestConf {

	@DConfigValue(value = "server.server_port")
	private static int port;

	public static int getPort() {
		return port;
	}

	@BeforeClass
	public static void init() {
		ClassScanner.getInstance().scanner();
	}

	@Test
	public void testConf() {
		Assert.assertEquals(8888, getPort());
	}
}
