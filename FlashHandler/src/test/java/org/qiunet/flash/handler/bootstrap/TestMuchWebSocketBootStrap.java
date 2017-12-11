package org.qiunet.flash.handler.bootstrap;

import com.google.protobuf.InvalidProtocolBufferException;
import io.netty.util.CharsetUtil;
import org.junit.Test;
import org.qiunet.flash.handler.common.message.MessageContent;
import org.qiunet.flash.handler.handler.proto.LoginProto;
import org.qiunet.flash.handler.netty.client.trigger.ILongConnResponseTrigger;
import org.qiunet.flash.handler.netty.client.websocket.NettyWebsocketClient;

import java.net.URI;
import java.util.concurrent.CountDownLatch;

/**
 * Created by qiunet.
 * 17/12/2
 */
public class TestMuchWebSocketBootStrap extends HttpBootStrap {
	private int clientCount = 100;
	private int requestCount = 10000;
	private CountDownLatch latch = new CountDownLatch(clientCount * requestCount);
	@Test
	public void testMuchWebSocket() throws InterruptedException {
		long start = System.currentTimeMillis();
		for (int i = 0; i < clientCount; i++) {
			new Thread(new Runnable() {
				@Override
				public void run() {
					NettyWebsocketClient client = new NettyWebsocketClient(URI.create("ws://localhost:8080/ws"), new Trigger());
					for (int j = 0; j < requestCount; j++) {
						String text = "testMuchWebSocket: "+j;
						byte [] bytes = text.getBytes(CharsetUtil.UTF_8);
						MessageContent content = new MessageContent(1005, bytes);
						client.sendMessage(content);
					}
				}
			}).start();
		}
		latch.await();
		long end = System.currentTimeMillis();
		System.out.println("All Time is:["+(end - start)+"]ms");
	}

	public class Trigger implements ILongConnResponseTrigger {
		@Override
		public void response(MessageContent data) {
			switch (data.getProtocolId()) {
				case 2000:
					System.out.println(new String(data.bytes(), CharsetUtil.UTF_8));
					break;
				case 2001:
					LoginProto.LoginResponse response = null;
					try {
						response = LoginProto.LoginResponse.parseFrom(data.bytes());
					} catch (InvalidProtocolBufferException e) {
						e.printStackTrace();
					}
					System.out.println(response.getTestString());
					break;
			}
			latch.countDown();
		}
	}
}
