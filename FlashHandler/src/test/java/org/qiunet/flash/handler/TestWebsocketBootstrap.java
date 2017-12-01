package org.qiunet.flash.handler;

import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketFrame;
import org.junit.Test;
import org.qiunet.flash.handler.bootstrap.HttpBootStrap;
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
		latch = new CountDownLatch(1);
		TextWebSocketFrame frame = new TextWebSocketFrame("Hello world");
		NettyWebsocketClient client = new NettyWebsocketClient(URI.create("ws://localhost:8080/ws"), new Trigger());

		client.sendTcpMessage(frame);

		latch.await();
	}

	public class  Trigger implements IWebsocketResponseTrigger {
		@Override
		public void response(WebSocketFrame webSocketFrame) {
			latch.countDown();
		}
	}
}
