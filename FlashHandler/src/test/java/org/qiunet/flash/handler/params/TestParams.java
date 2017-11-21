package org.qiunet.flash.handler.params;

import org.junit.Assert;
import org.junit.Test;
import org.qiunet.flash.handler.netty.param.HttpBootstrapParams;
import org.qiunet.flash.handler.netty.param.TcpBootstrapParams;

import java.net.InetAddress;
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
				.setAddress(new InetSocketAddress(InetAddress.getByName("localhost"), 1314))
				.setSsl(true)
				.setString(false)
				.build();
		Assert.assertEquals(1314, ((InetSocketAddress) params.getAddress()).getPort());
		Assert.assertEquals("localhost", ((InetSocketAddress) params.getAddress()).getHostString());
		Assert.assertEquals(true, params.isSsl());
		Assert.assertEquals(false, params.isString());

		TcpBootstrapParams tcpBootstrapParams = TcpBootstrapParams.custom().setAddress(new InetSocketAddress(InetAddress.getByName("127.0.0.1"), 1315)).build();
		Assert.assertEquals(1315, ((InetSocketAddress) tcpBootstrapParams.getAddress()).getPort());
		Assert.assertEquals("127.0.0.1", ((InetSocketAddress) tcpBootstrapParams.getAddress()).getHostString());
	}
}
