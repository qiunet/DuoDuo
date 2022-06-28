package org.qiunet.test.cross.client;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.qiunet.flash.handler.context.sender.IChannelMessageSender;
import org.qiunet.flash.handler.netty.client.param.WebSocketClientParams;
import org.qiunet.flash.handler.netty.client.websocket.NettyWebSocketClient;
import org.qiunet.test.cross.common.Constants;
import org.qiunet.test.cross.common.proto.req.EquipIndexRequest;
import org.qiunet.test.cross.common.proto.req.LoginRequest;
import org.qiunet.utils.scanner.ClassScanner;
import org.qiunet.utils.scanner.ScannerType;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.LockSupport;

/***
 *
 *
 * @author qiunet
 * 2020-10-23 10:34
 */
public class Client {
	private static IChannelMessageSender websocketClient;
	private static final AtomicInteger counter = new AtomicInteger();
	@BeforeAll
	public static void connect(){
		ClassScanner.getInstance(ScannerType.CLIENT).scanner();
		websocketClient = NettyWebSocketClient.create(WebSocketClientParams.custom().setAddress("localhost", Constants.LOGIC_SERVER_PORT).build(),
		(session, data) -> {
			System.out.println(counter.incrementAndGet() + "--------------------------"+data.getProtocolId());
		});
	}

	@Test
	public void request() throws InterruptedException {
		websocketClient.sendMessage(LoginRequest.valueOf(100000));
		websocketClient.sendMessage(new EquipIndexRequest());
		Thread.sleep(2000);
		//// 第二次将转发到Cross服务
		for (int i = 0; i < 10; i++) {
			websocketClient.sendMessage(new EquipIndexRequest());
		}
		LockSupport.park();
	}
}
