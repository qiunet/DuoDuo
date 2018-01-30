package org.qiunet.flash.handler.bootstrap;

import com.alibaba.fastjson.JSONObject;
import com.google.protobuf.InvalidProtocolBufferException;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.util.CharsetUtil;
import io.netty.util.ReferenceCountUtil;
import org.junit.Assert;
import org.junit.Test;
import org.qiunet.flash.handler.common.message.MessageContent;
import org.qiunet.flash.handler.context.header.ProtocolHeader;
import org.qiunet.flash.handler.context.request.http.json.JsonRequest;
import org.qiunet.flash.handler.context.response.json.JsonResponse;
import org.qiunet.flash.handler.context.status.IGameStatus;
import org.qiunet.flash.handler.handler.proto.LoginProto;
import org.qiunet.flash.handler.netty.client.trigger.IHttpResponseTrigger;
import org.qiunet.flash.handler.netty.client.http.NettyHttpClient;
import org.qiunet.utils.encryptAndDecrypt.CrcUtil;

import java.util.concurrent.locks.LockSupport;

/**
 * 测试接收数据
 * Created by qiunet.
 * 17/11/22
 */
public class TestHttpBootStrap extends HttpBootStrap {
	@Test
	public void testOtherHttpProtobuf() throws InvalidProtocolBufferException {
		final String test = "测试[testOtherHttpProtobuf]";
		LoginProto.LoginRequest request = LoginProto.LoginRequest.newBuilder().setTestString(test).build();
		ByteBuf byteBuf = Unpooled.wrappedBuffer(request.toByteArray());
		final Thread currThread = Thread.currentThread();
		NettyHttpClient.sendRequest(byteBuf, "http://localhost:8080/protobufTest", new IHttpResponseTrigger() {
			@Override
			public void response(FullHttpResponse httpResponse) {
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
			}
		});
		LockSupport.park();

	}

	@Test
	public void testHttpProtobuf() throws InvalidProtocolBufferException {
		final String test = "[测试testHttpProtobuf]";
		LoginProto.LoginRequest request = LoginProto.LoginRequest.newBuilder().setTestString(test).build();
		MessageContent content = new MessageContent(1001, request.toByteArray());
		final Thread currThread = Thread.currentThread();
		NettyHttpClient.sendRequest(content.encodeToByteBuf(), "http://localhost:8080/f", new IHttpResponseTrigger() {
			@Override
			public void response(FullHttpResponse httpResponse) {
				Assert.assertEquals(httpResponse.status(), HttpResponseStatus.OK);

				new ProtocolHeader(httpResponse.content());
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
			}
		});
		LockSupport.park();
	}

	/***
	 * 测试游戏http string的请求
	 */
	@Test
	public void testHttpString() throws InvalidProtocolBufferException {
		final String test = "测试[testHttpString]";
		MessageContent content = new MessageContent(1000, test.getBytes(CharsetUtil.UTF_8));
		final Thread currThread = Thread.currentThread();
		NettyHttpClient.sendRequest(content.encodeToByteBuf(), "http://localhost:8080/f", new IHttpResponseTrigger() {
			@Override
			public void response(FullHttpResponse httpResponse) {
				Assert.assertEquals(httpResponse.status(), HttpResponseStatus.OK);

				new ProtocolHeader(httpResponse.content());
				Assert.assertEquals(test, httpResponse.content().toString(CharsetUtil.UTF_8));
				ReferenceCountUtil.release(httpResponse);
				LockSupport.unpark(currThread);
			}
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
		NettyHttpClient.sendRequest(Unpooled.wrappedBuffer(test.getBytes(CharsetUtil.UTF_8)), "Http://localhost:8080/back?a=b", new IHttpResponseTrigger() {
			@Override
			public void response(FullHttpResponse httpResponse) {
				Assert.assertEquals(httpResponse.status(), HttpResponseStatus.OK);
				Assert.assertEquals(httpResponse.content().toString(CharsetUtil.UTF_8), test);
				ReferenceCountUtil.release(httpResponse);
				LockSupport.unpark(currThread);
			}
		});
		LockSupport.park();

	}
	@Test
	public void testHttpJson() throws InvalidProtocolBufferException {
		final String test = "测试[testHttpJson]";
		final JsonRequest request = new JsonRequest();
		request.addAttribute("test", test);

		MessageContent content = new MessageContent(1007, request.toString().getBytes(CharsetUtil.UTF_8));
		final Thread currThread = Thread.currentThread();
		NettyHttpClient.sendRequest(content.encodeToByteBuf(), "http://localhost:8080/f", new IHttpResponseTrigger() {
			@Override
			public void response(FullHttpResponse httpResponse) {
				Assert.assertEquals(httpResponse.status(), HttpResponseStatus.OK);

				new ProtocolHeader(httpResponse.content());
				JsonResponse response = JsonResponse.parse(httpResponse.content().toString(CharsetUtil.UTF_8));
				Assert.assertEquals(test, response.getString("test"));
				ReferenceCountUtil.release(httpResponse);
				LockSupport.unpark(currThread);
			}
		});
		LockSupport.park();

	}
	@Test
	public void testOtherHttpJson(){
		final String test = "测试[testOtherHttpJson]";
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("test",test);
		final Thread currThread = Thread.currentThread();

		NettyHttpClient.sendRequest(Unpooled.wrappedBuffer(jsonObject.toJSONString().getBytes(CharsetUtil.UTF_8)), "Http://localhost:8080/jsonUrl", new IHttpResponseTrigger() {
			@Override
			public void response(FullHttpResponse httpResponse) {
				Assert.assertEquals(httpResponse.status(), HttpResponseStatus.OK);
				String responseString = httpResponse.content().toString(CharsetUtil.UTF_8);
				JsonResponse response = JsonResponse.parse(responseString);
				Assert.assertEquals(response.status(), IGameStatus.SUCCESS.getStatus());
				Assert.assertEquals(response.get("test"), test);
				ReferenceCountUtil.release(httpResponse);
				LockSupport.unpark(currThread);
			}
		});
		LockSupport.park();
	}

}
