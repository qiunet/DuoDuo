package org.qiunet.flash.handler.params;

import org.junit.Assert;
import org.junit.Test;
import org.qiunet.flash.handler.interceptor.DefaultHttpInterceptor;
import org.qiunet.flash.handler.interceptor.DefaultTcpInterceptor;
import org.qiunet.flash.handler.netty.server.param.HttpBootstrapParams;
import org.qiunet.flash.handler.netty.server.param.TcpBootstrapParams;

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
				.setSsl(true)
				.setInterceptor(new DefaultHttpInterceptor())
				.build();
		Assert.assertEquals(1314, ((InetSocketAddress) params.getAddress()).getPort());
		Assert.assertEquals(true, params.isSsl());

		TcpBootstrapParams tcpBootstrapParams = TcpBootstrapParams.custom()
				.setPort(1315)
				.setMaxReceivedLength(1024*1024)
				.setInterceptor(new DefaultTcpInterceptor())
				.build();
		Assert.assertEquals(1315, ((InetSocketAddress) tcpBootstrapParams.getAddress()).getPort());
	}
}
