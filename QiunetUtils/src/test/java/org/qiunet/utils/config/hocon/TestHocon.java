package org.qiunet.utils.config.hocon;

import com.typesafe.config.Config;
import org.junit.Assert;
import org.junit.Test;

public class TestHocon {
	@Test
	public void testConf() {
		Config config = HoconUtil.loadConf("test.conf");
		Assert.assertEquals(8886, config.getInt("server.node_port"));
		Assert.assertEquals(8887, config.getInt("server.hook_port"));
		Assert.assertEquals(8888, config.getInt("server.server_port"));

		System.out.println(config.hasPath("db.global_db"));
	}
}
