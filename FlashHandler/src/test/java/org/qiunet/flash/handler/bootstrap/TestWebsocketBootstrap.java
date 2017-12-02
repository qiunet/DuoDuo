package org.qiunet.flash.handler.bootstrap;

import io.netty.handler.codec.http.websocketx.WebSocketFrame;
import io.netty.util.CharsetUtil;
import org.junit.Test;
import org.qiunet.flash.handler.bootstrap.HttpBootStrap;
import org.qiunet.flash.handler.context.header.MessageContent;
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
	@Test
	public void testWebsocket() throws InterruptedException {
		NettyWebsocketClient client = new NettyWebsocketClient(URI.create("ws://localhost:8080/ws"), new Trigger());

		latch = new CountDownLatch(1);
		String text = "Hello world";
		byte [] bytes = text.getBytes(CharsetUtil.UTF_8);
		MessageContent content = new MessageContent(1005, bytes);
		client.sendTcpMessage(content);

		latch.await();
	}

	public class  Trigger implements IWebsocketResponseTrigger {
		@Override
		public void response(WebSocketFrame webSocketFrame) {
			latch.countDown();
		}
	}
}
