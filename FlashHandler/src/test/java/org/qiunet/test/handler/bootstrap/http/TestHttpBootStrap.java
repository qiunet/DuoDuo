package org.qiunet.test.handler.bootstrap.http;

import com.alibaba.fastjson.JSONObject;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.util.CharsetUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.qiunet.flash.handler.common.protobuf.ProtobufDataManager;
import org.qiunet.flash.handler.context.status.IGameStatus;
import org.qiunet.flash.handler.netty.client.param.HttpClientParams;
import org.qiunet.test.handler.handler.http.JTestResponseData;
import org.qiunet.test.handler.proto.HttpPbLoginRequest;
import org.qiunet.test.handler.proto.LoginResponse;
import org.qiunet.test.handler.proto.ProtocolId;
import org.qiunet.utils.http.HttpRequest;
import org.qiunet.utils.json.JsonUtil;

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
		final Thread currThread = Thread.currentThread();
		HttpRequest.post(params.getURI())
			.withBytes(ADAPTER.getAllBytes(ProtocolId.Test.HTTP_PB_LOGIN_REQ, request.toByteBuffer()))
			.asyncExecutor((call, resp) -> {
				ByteBuffer buffer = ByteBuffer.wrap(resp.body().bytes());
				// 跳过头
				buffer.position(ADAPTER.getReqHeaderLength());

				LoginResponse loginResponse = ProtobufDataManager.decode(LoginResponse.class, buffer);
				Assertions.assertEquals(test, loginResponse.getTestString());
				LockSupport.unpark(currThread);
		});
		LockSupport.park();
	}


	/***
	 * 测试非游戏http string的请求
	 */
	@Test
	public void testOtherHttpString(){
		final String test = "p1=val1&p2=val2";
		final Thread currThread = Thread.currentThread();
		HttpRequest.post(params.getURI("/back?a=b"))
			.withBytes(test.getBytes(CharsetUtil.UTF_8))
			.asyncExecutor((call, httpResponse) -> {
				Assertions.assertEquals(httpResponse.code(), HttpResponseStatus.OK.code());
				Assertions.assertEquals(httpResponse.body().string(), test);
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
				Assertions.assertEquals(httpResponse.code(), HttpResponseStatus.OK.code());
				String responseString = httpResponse.body().string();

				JTestResponseData data = JsonUtil.getGeneralObjWithField(responseString, JTestResponseData.class);
				Assertions.assertEquals(data.getStatus().getCode(), IGameStatus.SUCCESS.getStatus());
				Assertions.assertEquals(data.getTest(), test);
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

		String respContent = HttpRequest.post("http://localhost:"+port+"/jsonUrl")
			.withJsonData(map)
			.executor();
		System.out.println("================="+ respContent);

	}
}
