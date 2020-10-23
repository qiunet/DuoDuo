package org.qiunet.flash.handler.bootstrap;

import io.netty.util.CharsetUtil;
import org.junit.Test;
import org.qiunet.flash.handler.common.message.MessageContent;
import org.qiunet.flash.handler.context.session.DSession;
import org.qiunet.flash.handler.netty.client.param.WebSocketClientParams;
import org.qiunet.flash.handler.netty.client.trigger.ILongConnResponseTrigger;
import org.qiunet.flash.handler.netty.client.websocket.NettyWebsocketClient;
import org.qiunet.flash.handler.proto.LoginResponse;
import org.qiunet.utils.protobuf.ProtobufDataManager;

import java.util.concurrent.CountDownLatch;

/**
 * Created by qiunet.
 * 17/12/2
 */
public class TestMuchWebSocketBootStrap extends HttpBootStrap {
	private int clientCount = 10;
	private int requestCount = 100;
	private CountDownLatch latch = new CountDownLatch(clientCount * requestCount);
	@Test
	public void testMuchWebSocket() throws InterruptedException {
		long start = System.currentTimeMillis();
		for (int i = 0; i < clientCount; i++) {
			new Thread(() -> {
				NettyWebsocketClient client = NettyWebsocketClient.create(WebSocketClientParams.custom()
					.setAddress("localhost", 8080)
					.setUriIPath("/ws")
					.build(), new Trigger());
				for (int j = 0; j < requestCount; j++) {
					String text = "testMuchWebSocket: "+j;
					byte [] bytes = text.getBytes(CharsetUtil.UTF_8);
					MessageContent content = new MessageContent(1005, bytes);
					client.sendMessage(content);
				}
			}).start();
		}
		latch.await();
		long end = System.currentTimeMillis();
		System.out.println("All Time is:["+(end - start)+"]ms");
	}

	public class Trigger implements ILongConnResponseTrigger {
		@Override
		public void response(DSession session, MessageContent data) {
			switch (data.getProtocolId()) {
				case 2000:
					System.out.println(new String(data.bytes(), CharsetUtil.UTF_8));
					break;
				case 2001:
					LoginResponse response = ProtobufDataManager.decode(LoginResponse.class, data.bytes());
					System.out.println(response.getTestString());
					break;
			}
			latch.countDown();
		}
	}
}
