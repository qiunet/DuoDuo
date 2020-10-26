package org.qiunet.flash.handler.bootstrap;

import com.alibaba.fastjson.JSONObject;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.util.CharsetUtil;
import io.netty.util.ReferenceCountUtil;
import org.junit.Assert;
import org.junit.Test;
import org.qiunet.flash.handler.common.message.MessageContent;
import org.qiunet.flash.handler.context.request.http.json.JsonRequest;
import org.qiunet.flash.handler.context.response.json.JsonResponse;
import org.qiunet.flash.handler.context.status.IGameStatus;
import org.qiunet.flash.handler.netty.client.http.NettyHttpClient;
import org.qiunet.flash.handler.netty.client.param.HttpClientParams;
import org.qiunet.flash.handler.proto.HttpPbLoginRequest;
import org.qiunet.flash.handler.proto.LoginResponse;
import org.qiunet.utils.http.HttpRequest;
import org.qiunet.utils.protobuf.ProtobufDataManager;

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
		NettyHttpClient.create(params).sendRequest(content, "/f", (httpResponse) -> {
			Assert.assertEquals(httpResponse.status(), HttpResponseStatus.OK);

			ADAPTER.newHeader(httpResponse.content());
			byte [] bytes = new byte[httpResponse.content().readableBytes()];
			httpResponse.content().readBytes(bytes);
			LoginResponse loginResponse = ProtobufDataManager.decode(LoginResponse.class, bytes);
			Assert.assertEquals(test, loginResponse.getTestString());
			ReferenceCountUtil.release(httpResponse);
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
		MessageContent content = new MessageContent(1000, test.getBytes(CharsetUtil.UTF_8));
		final Thread currThread = Thread.currentThread();
		NettyHttpClient.create(params).sendRequest(content, "/f", (response) -> {
			Assert.assertEquals(response.status(), HttpResponseStatus.OK);

			ADAPTER.newHeader(response.content());
			Assert.assertEquals(test, response.content().toString(CharsetUtil.UTF_8));
			ReferenceCountUtil.release(response);
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
		NettyHttpClient.create(params).sendRequest(new MessageContent(0, test.getBytes(CharsetUtil.UTF_8)),
			"/back?a=b", (httpResponse) -> {
				Assert.assertEquals(httpResponse.status(), HttpResponseStatus.OK);
				Assert.assertEquals(httpResponse.content().toString(CharsetUtil.UTF_8), test);
				ReferenceCountUtil.release(httpResponse);
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
		NettyHttpClient.create(params).sendRequest(content, "/f", (httpResponse) -> {
			Assert.assertEquals(httpResponse.status(), HttpResponseStatus.OK);

			ADAPTER.newHeader(httpResponse.content());
			JsonResponse response = JsonResponse.parse(httpResponse.content().toString(CharsetUtil.UTF_8));
			Assert.assertEquals(test, response.getString("test"));
			ReferenceCountUtil.release(httpResponse);
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

		NettyHttpClient.create(params).sendRequest(new MessageContent(0, jsonObject.toJSONString().getBytes(CharsetUtil.UTF_8)),
			"/jsonUrl", (httpResponse) -> {
				Assert.assertEquals(httpResponse.status(), HttpResponseStatus.OK);
				String responseString = httpResponse.content().toString(CharsetUtil.UTF_8);
				JsonResponse response = JsonResponse.parse(responseString);
				Assert.assertEquals(response.status(), IGameStatus.SUCCESS.getStatus());
				Assert.assertEquals(response.get("test"), test);
				ReferenceCountUtil.release(httpResponse);
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
