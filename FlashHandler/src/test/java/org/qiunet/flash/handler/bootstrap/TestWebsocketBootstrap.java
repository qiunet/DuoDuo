package org.qiunet.flash.handler.bootstrap;

import org.junit.Assert;
import org.junit.Test;
import org.qiunet.flash.handler.common.message.MessageContent;
import org.qiunet.flash.handler.context.session.DSession;
import org.qiunet.flash.handler.netty.client.param.WebSocketClientParams;
import org.qiunet.flash.handler.netty.client.trigger.IPersistConnResponseTrigger;
import org.qiunet.flash.handler.netty.client.websocket.NettyWebsocketClient;
import org.qiunet.flash.handler.proto.LoginResponse;
import org.qiunet.flash.handler.proto.WsPbLoginRequest;
import org.qiunet.utils.logger.LoggerType;
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
		NettyWebsocketClient client = NettyWebsocketClient.create(WebSocketClientParams.custom()
			.setAddress("localhost", port).build(), new ResponseTrigger());
		WsPbLoginRequest request = WsPbLoginRequest.valueOf(text, text, 11);
		latch = new CountDownLatch(1);

		client.sendMessage(request);
		latch.await();
	}

	public class ResponseTrigger implements IPersistConnResponseTrigger {
		@Override
		public void response(DSession session, MessageContent data) {
			// test 的地方.直接使用bytes 解析. 免得release
			LoginResponse response = ProtobufDataManager.decode(LoginResponse.class, data.bytes());
			LoggerType.DUODUO_FLASH_HANDLER.info("=WS Response Text:[{}]" , response.getTestString());
			Assert.assertEquals(text, response.getTestString());
			latch.countDown();
		}
	}
}
