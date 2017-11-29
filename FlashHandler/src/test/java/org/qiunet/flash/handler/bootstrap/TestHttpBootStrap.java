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
import org.qiunet.flash.handler.handler.proto.LoginProto;
import org.qiunet.flash.handler.netty.client.http.IHttpResponseTrigger;
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
		ByteBuf byteBuf = Unpooled.buffer();
		LoginProto.LoginRequest request = LoginProto.LoginRequest.newBuilder().setTestString(test).build();
		byte [] bytes = request.toByteArray();
		byteBuf.writeBytes(bytes);
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
		ByteBuf byteBuf = Unpooled.buffer();
		LoginProto.LoginRequest request = LoginProto.LoginRequest.newBuilder().setTestString(test).build();
		byte [] bytes = request.toByteArray();
		ProtocolHeader header = new ProtocolHeader(bytes.length, 1001, (int) CrcUtil.getCrc32Value(bytes));
		header.writeToByteBuf(byteBuf);
		byteBuf.writeBytes(bytes);
		final Thread currThread = Thread.currentThread();
		NettyHttpClient.sendRequest(byteBuf, "http://localhost:8080/f", new IHttpResponseTrigger() {
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
		ByteBuf byteBuf = Unpooled.buffer();
		byte [] bytes = test.getBytes(CharsetUtil.UTF_8);
		ProtocolHeader header = new ProtocolHeader(bytes.length, 1000, (int) CrcUtil.getCrc32Value(bytes));
		header.writeToByteBuf(byteBuf);

		byteBuf.writeBytes(bytes);
		final Thread currThread = Thread.currentThread();
		NettyHttpClient.sendRequest(byteBuf, "http://localhost:8080/f", new IHttpResponseTrigger() {
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
}
