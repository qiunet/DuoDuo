package org.qiunet.test.handler.bootstrap.ws;

import com.google.common.collect.Lists;
import org.junit.jupiter.api.Test;
import org.qiunet.flash.handler.common.message.MessageContent;
import org.qiunet.flash.handler.common.protobuf.ProtobufDataManager;
import org.qiunet.flash.handler.context.sender.IChannelMessageSender;
import org.qiunet.flash.handler.context.session.ISession;
import org.qiunet.flash.handler.netty.client.param.WebSocketClientParams;
import org.qiunet.flash.handler.netty.client.trigger.IPersistConnResponseTrigger;
import org.qiunet.flash.handler.netty.client.websocket.NettyWebSocketClient;
import org.qiunet.function.badword.DefaultBadWord;
import org.qiunet.function.badword.LoadBadWordEventData;
import org.qiunet.test.handler.bootstrap.http.HttpBootStrap;
import org.qiunet.test.handler.proto.LoginResponse;
import org.qiunet.test.handler.proto.WsPbLoginRequest;
import org.qiunet.utils.logger.LoggerType;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by qiunet.
 * 17/12/2
 */
public class TestMuchWebSocketRequest extends HttpBootStrap {
	private final int clientCount = 100;
	private final int requestCount = 1000;
	private final AtomicInteger counter = new AtomicInteger();
	private final CountDownLatch latch = new CountDownLatch(clientCount * requestCount);
	@Test
	public void testMuchWebSocket() throws InterruptedException {
		LoadBadWordEventData.valueOf(new DefaultBadWord(Lists.newArrayList("毛泽东"))).fireEventHandler();
		long start = System.currentTimeMillis();
		for (int i = 0; i < clientCount; i++) {
			new Thread(() -> {
				IChannelMessageSender client = NettyWebSocketClient.create(WebSocketClientParams.custom()
					.setAddress("localhost", port).build(), new Trigger());
				for (int j = 0; j < requestCount; j++) {
					String text = "testMuchWebSocket: "+j;
					WsPbLoginRequest wsPbLoginRequest = WsPbLoginRequest.valueOf(text, text, 99);
					client.sendMessage(wsPbLoginRequest);
				}
			}).start();
		}
		latch.await();
		long end = System.currentTimeMillis();
		System.out.println("All Time is:["+(end - start)+"]ms");
	}

	public class Trigger implements IPersistConnResponseTrigger {
		@Override
		public void response(ISession session, MessageContent data) {
			// test 的地方.直接使用bytes 解析. 免得release
			LoginResponse response = ProtobufDataManager.decode(LoginResponse.class, data.byteBuffer());
			LoggerType.DUODUO_FLASH_HANDLER.info("count: {} , content: {}", counter.incrementAndGet(), response.getTestString());
			latch.countDown();
		}
	}
}
