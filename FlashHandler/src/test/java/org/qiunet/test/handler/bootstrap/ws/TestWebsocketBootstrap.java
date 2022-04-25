package org.qiunet.test.handler.bootstrap.ws;

import com.google.common.collect.Lists;
import org.junit.jupiter.api.Assertions;
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

/**
 * Created by qiunet.
 * 17/12/1
 */
public class TestWebsocketBootstrap extends HttpBootStrap {
	private CountDownLatch latch;
	private String text;

	@Test
	public void testProtobufWebSocket() throws InterruptedException {
		LoadBadWordEventData.valueOf(new DefaultBadWord(Lists.newArrayList("毛泽东"))).fireEventHandler();
		text = "test [testProtobufWebSocket]";
		IChannelMessageSender client = NettyWebSocketClient.create(WebSocketClientParams.custom()
			.setAddress("localhost", port).build(), new ResponseTrigger());
		WsPbLoginRequest request = WsPbLoginRequest.valueOf(text, text, 11);
		latch = new CountDownLatch(1);

		client.sendMessage(request);
		latch.await();
	}

	public class ResponseTrigger implements IPersistConnResponseTrigger {
		@Override
		public void response(ISession session, MessageContent data) {
			// test 的地方.直接使用bytes 解析. 免得release
			LoginResponse response = ProtobufDataManager.decode(LoginResponse.class, data.bytes());
			LoggerType.DUODUO_FLASH_HANDLER.info("=WS Response Text:[{}]" , response.getTestString());
			Assertions.assertEquals(text, response.getTestString());
			latch.countDown();
		}
	}
}
