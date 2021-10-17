package org.qiunet.flash.handler.bootstrap;

import com.alibaba.fastjson.JSONObject;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.util.CharsetUtil;
import org.junit.Assert;
import org.junit.Test;
import org.qiunet.flash.handler.common.message.MessageContent;
import org.qiunet.flash.handler.context.response.json.JsonResponse;
import org.qiunet.flash.handler.context.status.IGameStatus;
import org.qiunet.flash.handler.netty.client.param.HttpClientParams;
import org.qiunet.flash.handler.proto.HttpPbLoginRequest;
import org.qiunet.flash.handler.proto.LoginResponse;
import org.qiunet.flash.handler.proto.ProtocolId;
import org.qiunet.utils.http.HttpRequest;
import org.qiunet.utils.protobuf.ProtobufDataManager;

import java.net.http.HttpResponse;
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
	private final HttpClientParams params = HttpClientParams.custom()
		.setAddress("localhost", port)
		.setProtocolHeaderType(ADAPTER)
		.build();

	@Test
	public void testHttpProtobuf() {
		final String test = "[测试testHttpProtobuf]";
		HttpPbLoginRequest request = HttpPbLoginRequest.valueOf(test, test, 11);
		MessageContent content = new MessageContent(ProtocolId.Test.HTTP_PB_LOGIN_REQ, request.toByteArray());
		final Thread currThread = Thread.currentThread();
		HttpRequest.post(params.getURI())
			.withBytes(ADAPTER.getAllBytes(content))
			.asyncExecutor(HttpResponse.BodyHandlers.ofByteArray(), (resp) -> {
				ByteBuffer buffer = ByteBuffer.wrap(resp.body());
				// 跳过头
				buffer.position(ADAPTER.getReqHeaderLength());

				LoginResponse loginResponse = ProtobufDataManager.decode(LoginResponse.class, buffer);
				Assert.assertEquals(test, loginResponse.getTestString());
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
			.asyncExecutor((httpResponse) -> {
				Assert.assertEquals(httpResponse.statusCode(), HttpResponseStatus.OK.code());
				Assert.assertEquals(httpResponse.body(), test);
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

		HttpRequest.post(params.getURI("/jsonUrl")).withBytes(bytes).asyncExecutor((httpResponse) -> {
				Assert.assertEquals(httpResponse.statusCode(), HttpResponseStatus.OK.code());
				String responseString = httpResponse.body();
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

		String respContent = HttpRequest.post(params.getURI("/jsonUrl"))
			.withJsonData(map)
			.executor();
		System.out.println("================="+ respContent);
	}
}
