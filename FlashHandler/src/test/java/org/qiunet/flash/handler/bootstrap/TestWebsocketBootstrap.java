package org.qiunet.flash.handler.bootstrap;

import com.google.protobuf.InvalidProtocolBufferException;
import io.netty.util.CharsetUtil;
import org.junit.Assert;
import org.junit.Test;
import org.qiunet.flash.handler.common.message.MessageContent;
import org.qiunet.flash.handler.handler.proto.LoginProto;
import org.qiunet.flash.handler.netty.client.param.WebSocketClientParams;
import org.qiunet.flash.handler.netty.client.trigger.ILongConnResponseTrigger;
import org.qiunet.flash.handler.netty.client.websocket.NettyWebsocketClient;

import java.util.concurrent.CountDownLatch;

/**
 * Created by qiunet.
 * 17/12/1
 */
public class TestWebsocketBootstrap extends HttpBootStrap {
	private CountDownLatch latch;
	private String text;
	@Test
	public void testStringWebsocket() throws InterruptedException {
		NettyWebsocketClient client = new NettyWebsocketClient(
			WebSocketClientParams.custom()
				.setAddress("localhost", 8080)
				.setUriIPath("/ws")
				.build()
			, new ResponseTrigger());
		text = "Hello world";
		latch = new CountDownLatch(1);

		byte [] bytes = text.getBytes(CharsetUtil.UTF_8);
		MessageContent content = new MessageContent(1005, bytes);
		client.sendMessage(content);

		latch.await();
	}

	@Test
	public void testProtobufWebSocket() throws InterruptedException {
		text = "test [testProtobufWebSocket]";
		NettyWebsocketClient client = new NettyWebsocketClient(WebSocketClientParams.custom()
			.setAddress("localhost", 8080)
			.setUriIPath("/ws")
			.build(), new ResponseTrigger());
		LoginProto.LoginRequest request = LoginProto.LoginRequest.newBuilder().setTestString(text).build();
		MessageContent content = new MessageContent(1006, request.toByteArray());
		latch = new CountDownLatch(1);

		client.sendMessage(content);
		latch.await();
	}

	public class ResponseTrigger implements ILongConnResponseTrigger {
		@Override
		public void response(MessageContent data) {
			switch (data.getProtocolId()) {
				case 2000:
					Assert.assertEquals(text, new String(data.bytes(), CharsetUtil.UTF_8));
					break;
				case 2001:
					LoginProto.LoginResponse response = null;
					try {
						response = LoginProto.LoginResponse.parseFrom(data.bytes());
					} catch (InvalidProtocolBufferException e) {
						e.printStackTrace();
					}
					Assert.assertEquals(text, response.getTestString());
					break;
			}
			latch.countDown();
		}
	}
}
