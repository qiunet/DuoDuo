package org.qiunet.flash.handler.params;

import org.junit.Assert;
import org.junit.Test;
import org.qiunet.flash.handler.netty.server.param.HttpBootstrapParams;
import org.qiunet.flash.handler.netty.server.param.TcpBootstrapParams;
import org.qiunet.flash.handler.startup.context.StartupContext;

import java.net.InetSocketAddress;
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
		Assert.assertEquals(1314, ((InetSocketAddress) params.getAddress()).getPort());

		TcpBootstrapParams tcpBootstrapParams = TcpBootstrapParams.custom()
				.setStartupContext(new StartupContext())
				.setMaxReceivedLength(1024*1024)
				.setPort(1315)
				.build();
		Assert.assertEquals(1315, ((InetSocketAddress) tcpBootstrapParams.getAddress()).getPort());
	}
}
