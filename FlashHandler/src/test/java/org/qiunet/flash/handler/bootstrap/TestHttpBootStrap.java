package org.qiunet.flash.handler.bootstrap;

import com.alibaba.fastjson.JSONObject;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.util.CharsetUtil;
import org.junit.Assert;
import org.junit.Test;
import org.qiunet.flash.handler.common.message.MessageContent;
import org.qiunet.flash.handler.context.header.IProtocolHeader;
import org.qiunet.flash.handler.context.request.http.json.JsonRequest;
import org.qiunet.flash.handler.context.response.json.JsonResponse;
import org.qiunet.flash.handler.context.status.IGameStatus;
import org.qiunet.flash.handler.netty.client.param.HttpClientParams;
import org.qiunet.flash.handler.proto.HttpPbLoginRequest;
import org.qiunet.flash.handler.proto.LoginResponse;
import org.qiunet.utils.http.HttpRequest;
import org.qiunet.utils.protobuf.ProtobufDataManager;

import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.LockSupport;

/**
 * 测试接收数据
 * Created by qiunet.
 * 17/11/22
 */
public class TestHttpBootStrap extends HttpBootStrap {
	private HttpClientParams params = HttpClientParams.custom()
		.setAddress("localhost", 8080)
		.setProtocolHeaderAdapter(ADAPTER)
		.build();

	@Test
	public void testHttpProtobuf() {
		final String test = "[测试testHttpProtobuf]";
		HttpPbLoginRequest request = HttpPbLoginRequest.valueOf(test, test, 11);
		MessageContent content = new MessageContent(2001, request.toByteArray());
		final Thread currThread = Thread.currentThread();
		HttpRequest.post(params.getURI())
			.withBytes(ADAPTER.getAllBytes(content))
			.asyncExecutor((call, resp) -> {
				ByteBuffer buffer = ByteBuffer.wrap(resp.body().bytes());
				IProtocolHeader header = ADAPTER.newHeader(buffer);

				byte [] bytes = new byte[header.getLength()];
				buffer.get(bytes);
				LoginResponse loginResponse = ProtobufDataManager.decode(LoginResponse.class, bytes);
				Assert.assertEquals(test, loginResponse.getTestString());
				LockSupport.unpark(currThread);
		});
		LockSupport.park();
	}

	/***
	 * 测试游戏http string的请求
	 */
	@Test
	public void testHttpString() {
		final String test = "测试[testHttpString]";
		MessageContent content = new MessageContent(5000, test.getBytes(CharsetUtil.UTF_8));
		final Thread currThread = Thread.currentThread();
		HttpRequest.post(params.getURI())
		.withBytes(ADAPTER.getAllBytes(content))
		.asyncExecutor((call, response) -> {
			Assert.assertEquals(response.code(), HttpResponseStatus.OK.code());

			ByteBuffer buffer = ByteBuffer.wrap(response.body().bytes());
			ADAPTER.newHeader(buffer);

			Assert.assertEquals(test, CharsetUtil.UTF_8.decode(buffer).toString());
			LockSupport.unpark(currThread);
		});
		LockSupport.park();

	}
	/***
	 * 测试非游戏http string的请求
	 */
	@Test
	public void testOtherHttpString(){
		final String test = "测试[testOtherHttpString]";
		final Thread currThread = Thread.currentThread();
		HttpRequest.post(params.getURI("/back?a=b"))
			.withBytes(test.getBytes(CharsetUtil.UTF_8))
			.asyncExecutor((call, httpResponse) -> {
				Assert.assertEquals(httpResponse.code(), HttpResponseStatus.OK.code());
				Assert.assertEquals(httpResponse.body().string(), test);
				LockSupport.unpark(currThread);

		});
		LockSupport.park();

	}
	@Test
	public void testHttpJson()  {
		final String test = "测试[testHttpJson]";
		final JsonRequest request = new JsonRequest();
		request.addAttribute("test", test);

		MessageContent content = new MessageContent(1007, request.toString().getBytes(CharsetUtil.UTF_8));
		final Thread currThread = Thread.currentThread();
		HttpRequest.post(params.getURI()).withBytes(ADAPTER.getAllBytes(content)).asyncExecutor((call, httpResponse) -> {
			Assert.assertEquals(httpResponse.code(), HttpResponseStatus.OK.code());

			ByteBuffer buffer = ByteBuffer.wrap(httpResponse.body().bytes());
			IProtocolHeader header = ADAPTER.newHeader(buffer);

			JsonResponse response = JsonResponse.parse(CharsetUtil.UTF_8.decode(buffer).toString());
			Assert.assertEquals(test, response.getString("test"));
			LockSupport.unpark(currThread);

		});
		LockSupport.park();

	}
	@Test
	public void testOtherHttpJson(){
		final String test = "测试[testOtherHttpJson]";
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("test",test);
		final Thread currThread = Thread.currentThread();
		byte[] bytes = jsonObject.toJSONString().getBytes(CharsetUtil.UTF_8);

		HttpRequest.post(params.getURI("/jsonUrl")).withBytes(bytes).asyncExecutor((call, httpResponse) -> {
				Assert.assertEquals(httpResponse.code(), HttpResponseStatus.OK.code());
				String responseString = httpResponse.body().string();
				JsonResponse response = JsonResponse.parse(responseString);
				Assert.assertEquals(response.status(), IGameStatus.SUCCESS.getStatus());
				Assert.assertEquals(response.get("test"), test);
				LockSupport.unpark(currThread);
		});
		LockSupport.park();
	}

	@Test
	public void testWithHttpClient() throws Exception {
				//        json方式
		Map<String, Object> map = new HashMap<>();
		map.put("uid", 30406L);
		map.put("Content", "fsdf--测试");
		map.put("SourceType" ,1);
		map.put("test" ,"myTest");

		String respContent = HttpRequest.post("http://localhost:8080/jsonUrl")
			.withJsonData(map)
			.executor();
		System.out.println("================="+ respContent);

	}
}
