package org.qiunet.flash.handler.bootstrap;

import io.netty.util.CharsetUtil;
import org.junit.Assert;
import org.junit.Test;
import org.qiunet.flash.handler.common.message.MessageContent;
import org.qiunet.flash.handler.netty.client.param.WebSocketClientParams;
import org.qiunet.flash.handler.netty.client.trigger.ILongConnResponseTrigger;
import org.qiunet.flash.handler.netty.client.websocket.NettyWebsocketClient;
import org.qiunet.flash.handler.proto.LoginRequest;
import org.qiunet.flash.handler.proto.LoginResponse;
import org.qiunet.utils.protobuf.ProtobufDataManager;

import java.util.concurrent.CountDownLatch;

/**
 * Created by qiunet.
 * 17/12/1
 */
public class TestWebsocketBootstrap extends HttpBootStrap {
	private CountDownLatch latch;
	private String text;

	@Test
	public void testProtobufWebSocket() throws InterruptedException {
		text = "test [testProtobufWebSocket]";
		NettyWebsocketClient client = new NettyWebsocketClient(WebSocketClientParams.custom()
			.setAddress("localhost", 8080)
			.setUriIPath("/ws")
			.build(), new ResponseTrigger());
		LoginRequest request = LoginRequest.valueOf(text, text, 11);
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
					LoginResponse response = ProtobufDataManager.decode(LoginResponse.class, data.bytes());
					Assert.assertEquals(text, response.getTestString());
					break;
			}
			latch.countDown();
		}
	}
}
