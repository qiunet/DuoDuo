package org.qiunet.test.handler.bootstrap.kcp;

import org.junit.jupiter.api.Test;
import org.qiunet.flash.handler.common.id.IProtocolId;
import org.qiunet.flash.handler.common.message.MessageContent;
import org.qiunet.flash.handler.common.protobuf.ProtobufDataManager;
import org.qiunet.flash.handler.context.session.ISession;
import org.qiunet.flash.handler.netty.client.kcp.NettyKcpClient;
import org.qiunet.flash.handler.netty.client.param.KcpClientParams;
import org.qiunet.flash.handler.netty.client.trigger.IPersistConnResponseTrigger;
import org.qiunet.flash.handler.netty.server.message.ConnectionReq;
import org.qiunet.test.handler.proto.LoginResponse;
import org.qiunet.test.handler.proto.ProtocolId;
import org.qiunet.test.handler.proto.TcpPbLoginRequest;
import org.qiunet.utils.logger.LoggerType;
import org.qiunet.utils.string.StringUtil;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 大量请求的泄漏测试
 * Created by qiunet.
 * 17/11/27
 */
public class TestMuchKcpRequest extends BasicKcpBootStrap {
	private final AtomicInteger counter = new AtomicInteger();
	private final int requestCount = 50000;
	private final int threadCount = 50;
	private final CountDownLatch latch = new CountDownLatch(requestCount);
	@Test
	public void muchRequest() throws InterruptedException {
		NettyKcpClient client = NettyKcpClient.create(KcpClientParams.DEFAULT_PARAMS, new Trigger());
		long start = System.currentTimeMillis();
		for (int j = 0; j < threadCount; j++) {
			new Thread(() -> {
				ISession connector = client.connect(host, port);
				connector.sendMessage(ConnectionReq.valueOf(StringUtil.randomString(10)));

				int count = requestCount/threadCount;
				for (int i = 0 ; i < count; i ++) {
					String text = "test [testKcpProtobuf]: "+i;
					TcpPbLoginRequest request = TcpPbLoginRequest.valueOf(text, text, 11, null);
					connector.sendMessage(request);
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
			if (data.getProtocolId() == IProtocolId.System.CONNECTION_RSP) {
				return;
			}

			if (data.getProtocolId() != ProtocolId.Test.LOGIN_RESP) {
				return;
			}

			LoginResponse response = ProtobufDataManager.decode(LoginResponse.class, data.byteBuffer());
			LoggerType.DUODUO_FLASH_HANDLER.info("count: {}, content: {}", counter.incrementAndGet(), response.getTestString());
			latch.countDown();
		}
	}
}
