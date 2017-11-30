package org.qiunet.flash.handler.bootstrap;

import com.google.protobuf.InvalidProtocolBufferException;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.util.CharsetUtil;
import io.netty.util.ReferenceCountUtil;
import org.junit.Assert;
import org.junit.Test;
import org.qiunet.flash.handler.context.header.ProtocolHeader;
import org.qiunet.flash.handler.netty.client.http.IHttpResponseTrigger;
import org.qiunet.flash.handler.netty.client.http.NettyHttpClient;
import org.qiunet.utils.encryptAndDecrypt.CrcUtil;

import java.util.concurrent.CountDownLatch;

/**
 * 大量请求的泄漏测试
 * Created by qiunet.
 * 17/11/27
 */
public class TestMuchHttpRequest extends HttpBootStrap {
	private int requestCount = 20000;
	private CountDownLatch latch = new CountDownLatch(requestCount);
	@Test
	public void muchRequest() throws InvalidProtocolBufferException, InterruptedException {
		long start = System.currentTimeMillis();
		final int threadCount = 20;
		for (int j = 0; j < threadCount; j++) {
			new Thread(new Runnable() {
				@Override
				public void run() {
					for (int i = 0; i < requestCount/threadCount; i++) {
						final String test = "[测试testHttpProtobuf]"+i;
						byte [] bytes = test.getBytes(CharsetUtil.UTF_8);
						ByteBuf byteBuf = Unpooled.buffer();
						ProtocolHeader header = new ProtocolHeader(bytes.length, 1000, (int) CrcUtil.getCrc32Value(bytes));
						header.writeToByteBuf(byteBuf);
						byteBuf.writeBytes(bytes);
						NettyHttpClient.sendRequest(byteBuf, "http://localhost:8080/f" , new IHttpResponseTrigger() {
							@Override
							public void response(FullHttpResponse response) {
								Assert.assertEquals(response.status() , HttpResponseStatus.OK);
								response.content().readBytes(new byte[ProtocolHeader.REQUEST_HEADER_LENGTH]);
								Assert.assertEquals(test, response.content().toString(CharsetUtil.UTF_8));
								ReferenceCountUtil.release(response);
								latch.countDown();
							}
						});
					}
				}
			}).start();
		}
		System.out.println("Over ["+(System.currentTimeMillis() - start)+"]ms");
		latch.await();
		long end = System.currentTimeMillis();
		System.out.println("All execute time is : ["+(end - start)+"]ms");
	}
}
