package org.qiunet.flash.handler.bootstrap;

import org.junit.Test;
import org.qiunet.flash.handler.common.message.MessageContent;
import org.qiunet.flash.handler.context.session.DSession;
import org.qiunet.flash.handler.netty.client.param.TcpClientParams;
import org.qiunet.flash.handler.netty.client.tcp.NettyTcpClient;
import org.qiunet.flash.handler.netty.client.trigger.ILongConnResponseTrigger;
import org.qiunet.flash.handler.proto.LoginResponse;
import org.qiunet.flash.handler.proto.TcpPbLoginRequest;
import org.qiunet.utils.protobuf.ProtobufDataManager;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.util.concurrent.CountDownLatch;

/**
 * 大量请求的泄漏测试
 * Created by qiunet.
 * 17/11/27
 */
public class TestMuchTcpRequest extends MuchTcpRequest {
	private int requestCount = 1000000;
	private CountDownLatch latch = new CountDownLatch(requestCount);
	@Test
	public void muchRequest() throws InterruptedException {
		long start = System.currentTimeMillis();
		final int threadCount = 100;
		for (int j = 0; j < threadCount; j++) {
			new Thread(() -> {
				NettyTcpClient tcpClient = null;
				try {
					tcpClient = NettyTcpClient.create(
						TcpClientParams.custom()
							.setAddress(new InetSocketAddress(InetAddress.getByName(host), port))
							.build()
						, new Trigger());
				} catch (UnknownHostException e) {
					e.printStackTrace();
				}
				int count = requestCount/threadCount;

				for (int i = 0 ; i < count; i ++) {
					String text = "test [testTcpProtobuf]: "+i;
					TcpPbLoginRequest request = TcpPbLoginRequest.valueOf(text, text, 11, null);
					MessageContent content = new MessageContent(3001, request.toByteArray());
					tcpClient.sendMessage(content);
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
			LoginResponse response = ProtobufDataManager.decode(LoginResponse.class, data.bytes());
			System.out.println(response.getTestString());
			latch.countDown();
		}
	}
}
