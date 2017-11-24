package org.qiunet.flash.handler.bootstrap;

import io.netty.buffer.Unpooled;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.util.CharsetUtil;
import io.netty.util.ReferenceCountUtil;
import org.junit.Assert;
import org.junit.Test;
import org.qiunet.flash.handler.bootstrap.hook.MyHook;
import org.qiunet.flash.handler.handler.mapping.RequestHandlerScanner;
import org.qiunet.flash.handler.interceptor.DefaultHttpStringInterceptor;
import org.qiunet.flash.handler.netty.client.http.NettyHttpClient;
import org.qiunet.flash.handler.netty.server.param.HttpBootstrapParams;
import org.qiunet.flash.handler.netty.server.BootstrapServer;

/**
 * 测试接收数据
 * Created by qiunet.
 * 17/11/22
 */
public class TestBootStrap extends RequestHandlerScanner {
	public static void main(String[] args) {

		HttpBootstrapParams httpParams = HttpBootstrapParams.custom()
				.setInterceptor(new DefaultHttpStringInterceptor())
				.setPort(8080)
				.build();
		BootstrapServer.createBootstrap(new MyHook()).httpListener(httpParams).await();
	}

	/***
	 *
	 */
	@Test
	public void testHttpString(){
		String test = "测试";
		NettyHttpClient httpClient = new NettyHttpClient("localhost", 8080);
		FullHttpResponse httpResponse = httpClient.sendRequest(Unpooled.wrappedBuffer(test.getBytes(CharsetUtil.UTF_8)), "/back?a=b");
		Assert.assertEquals(httpResponse.status(), HttpResponseStatus.OK);
		Assert.assertEquals(httpResponse.content().toString(CharsetUtil.UTF_8), test);
		ReferenceCountUtil.release(httpResponse);
	}
}
