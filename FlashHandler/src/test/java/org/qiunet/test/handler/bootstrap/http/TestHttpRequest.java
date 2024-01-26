package org.qiunet.test.handler.bootstrap.http;

import com.alibaba.fastjson2.JSONObject;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.util.CharsetUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.qiunet.flash.handler.common.protobuf.ProtobufDataManager;
import org.qiunet.flash.handler.context.status.IGameStatus;
import org.qiunet.flash.handler.netty.client.param.HttpClientConfig;
import org.qiunet.test.handler.handler.http.JTestResponseData;
import org.qiunet.test.handler.proto.HttpPbLoginRequest;
import org.qiunet.test.handler.proto.LoginResponse;
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
public class TestHttpRequest extends HttpBootStrap {
	private final HttpClientConfig params = HttpClientConfig.custom()
		.setAddress("localhost", port).build();

	@Test
	public void testHttpProtobuf() {
		final String test = "[测试testHttpProtobuf]";
		HttpPbLoginRequest request = HttpPbLoginRequest.valueOf(test, test, 11);
		final Thread currThread = Thread.currentThread();
		HttpRequest.post(params.getURI())
			.withBytes(this.getAllBytes(request.buildChannelMessage()))
			.asyncExecutor(resp -> {
				ByteBuffer buffer = ByteBuffer.wrap(resp.getBytes());
				// 跳过头
				buffer.position(PROTOCOL_HEADER.getClientInHeadLength());

                LoginResponse loginResponse;
                try {
                    loginResponse = ProtobufDataManager.decode(LoginResponse.class, buffer);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
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
		HttpRequest.post(params.getURI("/back.do?a=b"))
			.withBytes(test.getBytes(CharsetUtil.UTF_8))
			.asyncExecutor(response -> {
				Assertions.assertEquals(response.getStatus(), HttpResponseStatus.OK);
				Assertions.assertEquals(response.body(), test);
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

		HttpRequest.post(params.getURI("/jsonUrl.do")).withBytes(bytes).asyncExecutor(response -> {
				Assertions.assertEquals(response.getStatus(), HttpResponseStatus.OK);
				String responseString = response.body();

				JTestResponseData data = JsonUtil.getGeneralObj(responseString, JTestResponseData.class);
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

		String respContent = HttpRequest.post("http://localhost:"+port+"/jsonUrl.do")
			.withJsonData(map)
			.executor();
		System.out.println("================="+ respContent);

	}
}
