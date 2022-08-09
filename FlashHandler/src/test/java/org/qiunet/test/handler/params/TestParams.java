package org.qiunet.test.handler.params;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.qiunet.flash.handler.netty.server.param.HttpBootstrapParams;
import org.qiunet.flash.handler.netty.server.param.TcpBootstrapParams;
import org.qiunet.test.handler.startup.context.StartupContext;

import java.net.UnknownHostException;

/**
 * Created by qiunet.
 * 17/11/20
 */
public class TestParams {
	@Test
	public void testBootstrapParam() throws UnknownHostException {
		HttpBootstrapParams params = HttpBootstrapParams.custom()
				.setPort(1314)
				.build();
		Assertions.assertEquals(1314, params.getPort());

		TcpBootstrapParams tcpBootstrapParams = TcpBootstrapParams.custom()
				.setStartupContext(new StartupContext())
				.setMaxReceivedLength(1024*1024)
				.setPort(1315)
				.build();
		Assertions.assertEquals(1315,  tcpBootstrapParams.getPort());
	}
}
