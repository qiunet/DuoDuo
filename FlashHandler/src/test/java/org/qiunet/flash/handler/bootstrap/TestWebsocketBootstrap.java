package org.qiunet.flash.handler.bootstrap;

import com.google.protobuf.InvalidProtocolBufferException;
import io.netty.handler.codec.http.websocketx.WebSocketFrame;
import io.netty.util.CharsetUtil;
import org.junit.Assert;
import org.junit.Test;
import org.qiunet.flash.handler.bootstrap.HttpBootStrap;
import org.qiunet.flash.handler.context.header.MessageContent;
import org.qiunet.flash.handler.handler.proto.LoginProto;
import org.qiunet.flash.handler.netty.client.websocket.IWebsocketResponseTrigger;
import org.qiunet.flash.handler.netty.client.websocket.NettyWebsocketClient;

import java.net.URI;
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
		NettyWebsocketClient client = new NettyWebsocketClient(URI.create("ws://localhost:8080/ws"), new Trigger());
		text = "Hello world";
		latch = new CountDownLatch(1);

		byte [] bytes = text.getBytes(CharsetUtil.UTF_8);
		MessageContent content = new MessageContent(1005, bytes);
		client.sendTcpMessage(content);

		latch.await();
	}

	public class Trigger implements IWebsocketResponseTrigger {
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
