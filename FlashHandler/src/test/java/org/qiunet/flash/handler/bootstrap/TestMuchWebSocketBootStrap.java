package org.qiunet.flash.handler.bootstrap;

import org.junit.Test;
import org.qiunet.flash.handler.common.message.MessageContent;
import org.qiunet.flash.handler.context.session.DSession;
import org.qiunet.flash.handler.netty.client.param.WebSocketClientParams;
import org.qiunet.flash.handler.netty.client.trigger.IPersistConnResponseTrigger;
import org.qiunet.flash.handler.netty.client.websocket.NettyWebsocketClient;
import org.qiunet.flash.handler.proto.LoginResponse;
import org.qiunet.flash.handler.proto.WsPbLoginRequest;
import org.qiunet.utils.protobuf.ProtobufDataManager;

import java.util.concurrent.CountDownLatch;

/**
 * Created by qiunet.
 * 17/12/2
 */
public class TestMuchWebSocketBootStrap extends HttpBootStrap {
	private int clientCount = 100;
	private int requestCount = 1000;
	private CountDownLatch latch = new CountDownLatch(clientCount * requestCount);
	@Test
	public void testMuchWebSocket() throws InterruptedException {
		long start = System.currentTimeMillis();
		for (int i = 0; i < clientCount; i++) {
			new Thread(() -> {
				NettyWebsocketClient client = NettyWebsocketClient.create(WebSocketClientParams.custom()
					.setAddress("localhost", port).build(), new Trigger());
				for (int j = 0; j < requestCount; j++) {
					String text = "testMuchWebSocket: "+j;
					WsPbLoginRequest wsPbLoginRequest = WsPbLoginRequest.valueOf(text, text, 1000);
					MessageContent content = new MessageContent(1006, wsPbLoginRequest.toByteArray());
					client.sendMessage(content);
				}
			}).start();
		}
		latch.await();
		long end = System.currentTimeMillis();
		System.out.println("All Time is:["+(end - start)+"]ms");
	}

	public class Trigger implements IPersistConnResponseTrigger {
		@Override
		public void response(DSession session, MessageContent data) {
			// test 的地方.直接使用bytes 解析. 免得release
			LoginResponse response = ProtobufDataManager.decode(LoginResponse.class, data.bytes());
			System.out.println(response.getTestString());
			latch.countDown();
		}
	}
}
