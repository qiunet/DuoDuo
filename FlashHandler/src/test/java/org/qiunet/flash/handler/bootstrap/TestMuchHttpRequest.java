package org.qiunet.flash.handler.bootstrap;

import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.util.CharsetUtil;
import io.netty.util.ReferenceCountUtil;
import org.junit.Assert;
import org.junit.Test;
import org.qiunet.flash.handler.common.message.MessageContent;
import org.qiunet.flash.handler.netty.client.http.NettyHttpClient;
import org.qiunet.flash.handler.netty.client.param.HttpClientParams;

import java.util.concurrent.CountDownLatch;

/**
 * 大量请求的泄漏测试
 * Created by qiunet.
 * 17/11/27
 */
public class TestMuchHttpRequest extends HttpBootStrap {
	private int requestCount = 10000;
	private CountDownLatch latch = new CountDownLatch(requestCount);
	private HttpClientParams params = HttpClientParams.custom()
		.setAddress("localhost", 8080)
		.setProtocolHeaderAdapter(ADAPTER)
		.build();
	@Test
	public void muchRequest() throws InterruptedException {
		long start = System.currentTimeMillis();
		final int threadCount = 20;
		for (int j = 0; j < threadCount; j++) {
			NettyHttpClient httpClient = NettyHttpClient.create(params);
			new Thread(() -> {
				for (int i = 0; i < requestCount/threadCount; i++) {
					final String test = "[测试testHttpProtobuf]"+i;

					MessageContent content = new MessageContent(1000, test.getBytes(CharsetUtil.UTF_8));
					httpClient.sendRequest(content, "/f", response -> {
						Assert.assertEquals(response.status() , HttpResponseStatus.OK);
						ADAPTER.newHeader(response.content());

						Assert.assertEquals(test, response.content().toString(CharsetUtil.UTF_8));
						ReferenceCountUtil.release(response);
						latch.countDown();
					});
				}
			}).start();
		}
		System.out.println("Over ["+(System.currentTimeMillis() - start)+"]ms");
		latch.await();
		long end = System.currentTimeMillis();
		System.out.println("All execute time is : ["+(end - start)+"]ms");
	}
}
