package org.qiunet.flash.handler.bootstrap;

import com.alibaba.fastjson.JSONObject;
import com.google.protobuf.InvalidProtocolBufferException;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.util.CharsetUtil;
import io.netty.util.ReferenceCountUtil;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.junit.Assert;
import org.junit.Test;
import org.qiunet.flash.handler.common.message.MessageContent;
import org.qiunet.flash.handler.context.request.http.json.JsonRequest;
import org.qiunet.flash.handler.context.response.json.JsonResponse;
import org.qiunet.flash.handler.context.status.IGameStatus;
import org.qiunet.flash.handler.handler.proto.LoginProto;
import org.qiunet.flash.handler.netty.client.http.NettyHttpClient;
import org.qiunet.flash.handler.netty.client.param.HttpClientParams;
import org.qiunet.utils.json.JsonUtil;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
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
		.setStartupContextAdapter(ADAPTER)
		.build();

	@Test
	public void testOtherHttpProtobuf() {
		final String test = "测试[testOtherHttpProtobuf]";
		LoginProto.LoginRequest request = LoginProto.LoginRequest.newBuilder().setTestString(test).build();
		final Thread currThread = Thread.currentThread();
		NettyHttpClient.create(params).sendRequest(new MessageContent(0, request.toByteArray()),
			"/protobufTest", (httpResponse) -> {
				Assert.assertEquals(httpResponse.status(), HttpResponseStatus.OK);

				byte[] bytes = new byte[httpResponse.content().readableBytes()];
				httpResponse.content().readBytes(bytes);
				LoginProto.LoginResponse loginResponse = null;
				try {
					loginResponse = LoginProto.LoginResponse.parseFrom(bytes);
				} catch (InvalidProtocolBufferException e) {
					e.printStackTrace();
				}
				Assert.assertEquals(test, loginResponse.getTestString());
				ReferenceCountUtil.release(httpResponse);
				LockSupport.unpark(currThread);
		});
		LockSupport.park();

	}

	@Test
	public void testHttpProtobuf() throws InvalidProtocolBufferException {
		final String test = "[测试testHttpProtobuf]";
		LoginProto.LoginRequest request = LoginProto.LoginRequest.newBuilder().setTestString(test).build();
		MessageContent content = new MessageContent(1001, request.toByteArray());
		final Thread currThread = Thread.currentThread();
		NettyHttpClient.create(params).sendRequest(content, "/f", (httpResponse) -> {
			Assert.assertEquals(httpResponse.status(), HttpResponseStatus.OK);

			ADAPTER.newHeader(httpResponse.content());
			byte [] bytes = new byte[httpResponse.content().readableBytes()];
			httpResponse.content().readBytes(bytes);
			LoginProto.LoginResponse loginResponse = null;
			try {
				loginResponse = LoginProto.LoginResponse.parseFrom(bytes);
			} catch (InvalidProtocolBufferException e) {
				e.printStackTrace();
			}
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
	public void testWithHttpClient() {
		HttpPost httpPost = new HttpPost("http://localhost:8080/jsonUrl");
		CloseableHttpClient client = HttpClients.createDefault();
		String respContent = null;

		//        json方式
		Map<String, Object> map = new HashMap<>();
		map.put("uid", 30406L);
		map.put("Content", "fsdf--测试");
		map.put("SourceType" ,1);
		map.put("test" ,"myTest");

		StringEntity entity = null;
		try {
			entity = new StringEntity(JsonUtil.toJsonString(map));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		entity.setContentEncoding("UTF-8");
		entity.setContentType("application/json");
		httpPost.setEntity(entity);
		try {
			HttpResponse resp = client.execute(httpPost);
			if(resp.getStatusLine().getStatusCode() == 200) {
				HttpEntity he = resp.getEntity();
				respContent = EntityUtils.toString(he,"UTF-8");
				System.out.println("================="+ respContent);
			}
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
