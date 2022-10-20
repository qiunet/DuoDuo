package org.qiunet.test.handler.params;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.qiunet.flash.handler.context.header.CompatibleProtocolHeader;
import org.qiunet.flash.handler.netty.server.param.ServerBootStrapParam;

import java.net.UnknownHostException;

/**
 * Created by qiunet.
 * 17/11/20
 */
public class TestParams {
	@Test
	public void testBootstrapParam() throws UnknownHostException {
		ServerBootStrapParam param = ServerBootStrapParam.newBuild("测试", 8888)
				.setTcpBootStrapParam(ServerBootStrapParam.TcpBootstrapParam.newBuild().setUdpOpen(ServerBootStrapParam.KcpBootstrapParam.newBuild().setPortCount(10).build()).build())
				.setHttpBootStrapParam(ServerBootStrapParam.HttpBootstrapParam.newBuild().setWebsocketPath("/aa").build())
				.setProtocolHeader(CompatibleProtocolHeader.instance)
				.setEncryption(true)
				.build();

		Assertions.assertEquals(param.getHttpParam().getWebsocketPath(), "/aa");
		Assertions.assertEquals(param.getKcpParam().getPorts().size(), 10);
		for (int i = 0; i < 10; i++) {
			Assertions.assertTrue(param.getKcpParam().getPorts().contains(i + 100 + param.getPort()));
		}

		Assertions.assertEquals(param.getProtocolHeader(), CompatibleProtocolHeader.instance);
		Assertions.assertEquals(param.getServerName(), "测试");
		Assertions.assertEquals(param.getPort(), 8888);
		Assertions.assertTrue(param.isEncryption());
	}
}
