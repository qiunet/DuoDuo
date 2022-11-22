package org.qiunet.test.handler.params;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.qiunet.flash.handler.context.header.CompatibleProtocolHeader;
import org.qiunet.flash.handler.netty.server.config.ServerBootStrapConfig;

import java.net.UnknownHostException;

/**
 * Created by qiunet.
 * 17/11/20
 */
public class TestParams {
	@Test
	public void testBootstrapParam() throws UnknownHostException {
		ServerBootStrapConfig config = ServerBootStrapConfig.newBuild("测试", 8888)
				.setTcpBootStrapConfig(ServerBootStrapConfig.TcpBootstrapConfig.newBuild().setUdpOpen(ServerBootStrapConfig.KcpBootstrapConfig.newBuild().setPortCount(10).build()).build())
				.setHttpBootStrapConfig(ServerBootStrapConfig.HttpBootstrapConfig.newBuild().setWebsocketPath("/aa").build())
				.setProtocolHeader(CompatibleProtocolHeader.instance)
				.encryption()
				.build();

		Assertions.assertEquals(config.getHttpBootstrapConfig().getWebsocketPath(), "/aa");
		Assertions.assertEquals(config.getKcpBootstrapConfig().getPorts().size(), 10);
		for (int i = 0; i < 10; i++) {
			Assertions.assertTrue(config.getKcpBootstrapConfig().getPorts().contains(i + 100 + config.getPort()));
		}

		Assertions.assertEquals(config.getProtocolHeader(), CompatibleProtocolHeader.instance);
		Assertions.assertEquals(config.getServerName(), "测试");
		Assertions.assertEquals(config.getPort(), 8888);
		Assertions.assertTrue(config.isEncryption());
	}
}
