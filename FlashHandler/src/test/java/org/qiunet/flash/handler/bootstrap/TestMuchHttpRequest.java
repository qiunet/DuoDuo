package org.qiunet.flash.handler.bootstrap;

import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.util.CharsetUtil;
import org.junit.Assert;
import org.junit.Test;
import org.qiunet.flash.handler.common.message.MessageContent;
import org.qiunet.flash.handler.netty.client.param.HttpClientParams;
import org.qiunet.utils.http.HttpRequest;

import java.nio.ByteBuffer;
import java.util.concurrent.CountDownLatch;

/**
 * 大量请求的泄漏测试
 * Created by qiunet.
 * 17/11/27
 */
public class TestMuchHttpRequest extends HttpBootStrap {
	private int requestCount = 5000;
	private CountDownLatch latch = new CountDownLatch(requestCount);
	private HttpClientParams params = HttpClientParams.custom()
		.setAddress("localhost", port)
		.setProtocolHeaderAdapter(ADAPTER)
		.build();
	@Test
	public void muchRequest() throws InterruptedException {
		long start = System.currentTimeMillis();
		final int threadCount = 20;
		for (int j = 0; j < threadCount; j++) {
			new Thread(() -> {
				for (int i = 0; i < requestCount/threadCount; i++) {
					final String test = "[测试testHttpProtobuf]"+i;

					MessageContent content = new MessageContent(5000, test.getBytes(CharsetUtil.UTF_8));
					HttpRequest.post(params.getURI())
						.withBytes(ADAPTER.getAllBytes(content))
						.asyncExecutor((call, response) -> {
						Assert.assertEquals(response.code() , HttpResponseStatus.OK.code());
						ByteBuffer buffer = ByteBuffer.wrap(response.body().bytes());
						ADAPTER.newHeader(buffer);

						Assert.assertEquals(test, CharsetUtil.UTF_8.decode(buffer).toString());
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
