package org.qiunet.test.handler.bootstrap.http;

import io.netty.handler.codec.http.HttpResponseStatus;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.qiunet.flash.handler.common.protobuf.ProtobufDataManager;
import org.qiunet.flash.handler.netty.client.param.HttpClientParams;
import org.qiunet.test.handler.proto.HttpPbLoginRequest;
import org.qiunet.test.handler.proto.LoginResponse;
import org.qiunet.utils.http.HttpRequest;

import java.nio.ByteBuffer;
import java.util.concurrent.CountDownLatch;

import static org.qiunet.test.handler.proto.ProtocolId.Test.HTTP_PB_LOGIN_REQ;

/**
 * 大量请求的泄漏测试
 * Created by qiunet.
 * 17/11/27
 */
public class TestMuchHttpRequest extends HttpBootStrap {
	private final int requestCount = 5000;
	private final CountDownLatch latch = new CountDownLatch(requestCount);
	private final HttpClientParams params = HttpClientParams.custom()
		.setAddress("localhost", port)
		.setProtocolHeaderType(ADAPTER)
		.build();
	@Test
	public void muchRequest() throws InterruptedException {
		long start = System.currentTimeMillis();
		final int threadCount = 20;
		for (int j = 0; j < threadCount; j++) {
			new Thread(() -> {
				for (int i = 0; i < requestCount/threadCount; i++) {
					final String test = "[测试testHttpProtobuf]"+i;
					HttpPbLoginRequest request = HttpPbLoginRequest.valueOf(test, test, 11);
					HttpRequest.post(params.getURI())
						.withBytes(ADAPTER.getAllBytes(HTTP_PB_LOGIN_REQ, request.toByteArray()))
						.asyncExecutor((call, response) -> {
							Assertions.assertEquals(response.code() , HttpResponseStatus.OK.code());
							ByteBuffer buffer = ByteBuffer.wrap(response.body().bytes());
							buffer.position(ADAPTER.getReqHeaderLength());

							LoginResponse loginResponse = ProtobufDataManager.decode(LoginResponse.class,buffer);
							Assertions.assertEquals(test, loginResponse.getTestString());
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
