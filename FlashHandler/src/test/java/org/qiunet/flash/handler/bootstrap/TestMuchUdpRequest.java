package org.qiunet.flash.handler.bootstrap;

import com.google.protobuf.InvalidProtocolBufferException;
import org.junit.Assert;
import org.junit.Test;
import org.qiunet.flash.handler.common.message.MessageContent;
import org.qiunet.flash.handler.handler.proto.LoginProto;
import org.qiunet.flash.handler.netty.client.trigger.ILongConnResponseTrigger;
import org.qiunet.flash.handler.netty.client.udp.NettyUdpClient;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 大量请求的泄漏测试
 * Created by qiunet.
 * 17/11/27
 */
public class TestMuchUdpRequest extends MuchUdpRequest {
	private int requestCount = 10000;
	private CountDownLatch latch = new CountDownLatch(requestCount);
	@Test
	public void muchRequest() throws InterruptedException {
		long start = System.currentTimeMillis();
		final int threadCount = 10;
		int count = requestCount/threadCount;
		for (int j = 0; j < threadCount; j++) {
			new Thread(() -> {
				NettyUdpClient tcpClient = null;
				try {
					tcpClient = new NettyUdpClient(new InetSocketAddress(InetAddress.getByName(host), port), new Trigger(Thread.currentThread().getName()),true);
				} catch (UnknownHostException e) {
					e.printStackTrace();
				}
				for (int i = 0 ; i < count; i ++) {
					String text = "count["+i+"] threadName:"+Thread.currentThread().getName();
					LoginProto.LoginRequest request = LoginProto.LoginRequest.newBuilder().setTestString(text).build();
					MessageContent content = new MessageContent(1009, request.toByteArray());
					tcpClient.sendMessage(content);
				}
			}, String.valueOf(j)).start();
		}

		latch.await();
		long end = System.currentTimeMillis();
		System.out.println("All Time is:["+(end - start)+"]ms");
	}
	private AtomicInteger atomicInteger = new AtomicInteger();
	public class Trigger implements ILongConnResponseTrigger {
		private String name;

		public Trigger(String name) {
			this.name = name;
		}

		@Override
		public void response(MessageContent data) {
			LoginProto.LoginResponse response = null;
			try {
				response = LoginProto.LoginResponse.parseFrom(data.bytes());
			} catch (InvalidProtocolBufferException e) {
				e.printStackTrace();
			}
			System.out.println(atomicInteger.incrementAndGet());
			// 对比推送对象是否是发送消息的发送者.
			Assert.assertEquals(this.name, response.getTestString().split(":")[1].trim());
			latch.countDown();
		}
	}
}
