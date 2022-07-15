package org.qiunet.test.handler.bootstrap.ws;

import com.google.common.collect.Lists;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.qiunet.flash.handler.common.id.IProtocolId;
import org.qiunet.flash.handler.common.message.MessageContent;
import org.qiunet.flash.handler.common.protobuf.ProtobufDataManager;
import org.qiunet.flash.handler.context.session.ISession;
import org.qiunet.flash.handler.netty.client.param.WebSocketClientParams;
import org.qiunet.flash.handler.netty.client.trigger.IPersistConnResponseTrigger;
import org.qiunet.flash.handler.netty.client.websocket.NettyWebSocketClient;
import org.qiunet.flash.handler.netty.server.message.ConnectionReq;
import org.qiunet.function.badword.DefaultBadWord;
import org.qiunet.function.badword.LoadBadWordEventData;
import org.qiunet.test.handler.bootstrap.http.HttpBootStrap;
import org.qiunet.test.handler.proto.LoginResponse;
import org.qiunet.test.handler.proto.WsPbLoginRequest;
import org.qiunet.utils.logger.LoggerType;
import org.qiunet.utils.string.StringUtil;

import java.util.concurrent.CountDownLatch;

/**
 * Created by qiunet.
 * 17/12/1
 */
public class TestWebsocketRequest extends HttpBootStrap {
	private final ISession client = NettyWebSocketClient.create(WebSocketClientParams.custom()
			.setAddress("localhost", port).build(), new ResponseTrigger());;
	private final CountDownLatch latch = new CountDownLatch(1);
	private String text;

	@Test
	public void testProtobufWebSocket() throws InterruptedException {
		LoadBadWordEventData.valueOf(new DefaultBadWord(Lists.newArrayList("毛泽东"))).fireEventHandler();
		text = "test [testProtobufWebSocket]";
		client.sendMessage(ConnectionReq.valueOf(StringUtil.randomString(5)), true);
		client.sendMessage(WsPbLoginRequest.valueOf(text, text, 11));
		latch.await();
	}

	public class ResponseTrigger implements IPersistConnResponseTrigger {
		@Override
		public void response(ISession session, MessageContent data) {
			if (data.getProtocolId() == IProtocolId.System.CONNECTION_RSP) {
				return;
			}

			// test 的地方.直接使用bytes 解析. 免得release
			LoginResponse response = ProtobufDataManager.decode(LoginResponse.class, data.byteBuffer());
			LoggerType.DUODUO_FLASH_HANDLER.info("=WS Response Text:[{}]" , response.getTestString());
			Assertions.assertEquals(text, response.getTestString());
			latch.countDown();
		}
	}
}
