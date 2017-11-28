package org.qiunet.flash.handler.bootstrap;

import com.google.protobuf.InvalidProtocolBufferException;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.handler.codec.http.HttpHeaderNames;
import io.netty.handler.codec.http.HttpHeaderValues;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.junit.Assert;
import org.junit.Test;
import org.qiunet.flash.handler.context.header.ProtocolHeader;
import org.qiunet.flash.handler.handler.proto.LoginProto;
import org.qiunet.utils.encryptAndDecrypt.CrcUtil;
import org.qiunet.utils.exceptions.UrlEmptyException;
import org.qiunet.utils.string.StringUtil;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

/**
 * 大量请求的泄漏测试
 * Created by qiunet.
 * 17/11/27
 */
public class TestMuchHttpRequest extends HttpBootStrap {
	private int requestCount = 10000;
	private CountDownLatch latch = new CountDownLatch(requestCount);
	/**
	 *
	 */
	@Test
	public void muchRequest() throws InvalidProtocolBufferException, InterruptedException {
		long start = System.currentTimeMillis();
		final int threadCount = 20;
		for (int i = 0; i < threadCount; i++) {
			new Thread(new Runnable() {
				@Override
				public void run() {
					int count = requestCount/ threadCount;
					for (int j = 0; j < count; j++) {
						try {
							testRequest(j);
						} catch (Exception e) {
							e.printStackTrace();
						}finally {
							latch.countDown();
						}
					}
				}
			}).start();
		}

		latch.await();
		long end = System.currentTimeMillis();
		System.out.println("All execute time is : ["+(end - start)+"]ms");
		Thread.sleep(3000);
	}

	private static void testRequest(int id) throws IOException {
		String test = "[测试testHttpProtobuf]"+id;
		ByteBuf byteBuf = Unpooled.buffer();
		LoginProto.LoginRequest request = LoginProto.LoginRequest.newBuilder().setTestString(test).build();
		byte [] bytes = request.toByteArray();
		ProtocolHeader header = new ProtocolHeader(bytes.length, 1001, (int) CrcUtil.getCrc32Value(bytes));
		header.writeToByteBuf(byteBuf);
		byteBuf.writeBytes(bytes);
		HttpResponse response = HttpRequestUtil.httpRequest(byteBuf, "http://localhost:8080/f");
		Assert.assertEquals(response.getStatusLine().getStatusCode() , HttpStatus.SC_OK);

		response.getEntity().getContent().read(new byte[ProtocolHeader.REQUEST_HEADER_LENGTH]);

		LoginProto.LoginResponse loginResponse = LoginProto.LoginResponse.parseFrom(response.getEntity().getContent());
		Assert.assertEquals(test, loginResponse.getTestString());
		System.out.println(loginResponse.getTestString());
	}

	public static class HttpRequestUtil {
		private static RequestConfig REQUESTCONFIG = RequestConfig.custom().setSocketTimeout(6000).setConnectTimeout(6000).build();
		private static HttpClientBuilder builder = HttpClientBuilder.create();
		/**
		 * 实现一个http 请求的方法
		 * @param url url
		 */
		public static HttpResponse httpRequest(ByteBuf bytebuf, String url){

			if(StringUtil.isEmpty(url)) throw new UrlEmptyException();

			HttpPost request = new HttpPost(url);
			HttpResponse response = null;
			try {
				request.setConfig(REQUESTCONFIG);
				byte [] bytes = new byte[bytebuf.readableBytes()];
				bytebuf.readBytes(bytes);
				request.setHeader(HttpHeaderNames.CONNECTION.toString(), HttpHeaderValues.KEEP_ALIVE.toString());
				request.setEntity(new ByteArrayEntity(bytes));
				HttpClient client = builder.build();
				response = client.execute(request);
			} catch (Exception e) {
				e.printStackTrace();
				if (request != null) {
					request.abort();
				}
			}
			return response;
		}
	}
}
