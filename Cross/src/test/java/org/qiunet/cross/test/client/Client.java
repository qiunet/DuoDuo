package org.qiunet.cross.test.client;

import org.junit.BeforeClass;
import org.junit.Test;
import org.qiunet.cross.test.common.Constants;
import org.qiunet.cross.test.proto.req.LoginRequest;
import org.qiunet.flash.handler.common.message.MessageContent;
import org.qiunet.flash.handler.netty.client.param.WebSocketClientParams;
import org.qiunet.flash.handler.netty.client.websocket.NettyWebsocketClient;
import org.qiunet.utils.scanner.ClassScanner;
import org.qiunet.utils.scanner.ScannerType;

import java.util.concurrent.locks.LockSupport;

/***
 *
 *
 * @author qiunet
 * 2020-10-23 10:34
 */
public class Client {
	private static NettyWebsocketClient websocketClient;
	@BeforeClass
	public static void connect(){
		ClassScanner.getInstance(ScannerType.TESTER).scanner();
		websocketClient = NettyWebsocketClient.create(WebSocketClientParams.custom().setAddress("localhost", Constants.LOGIC_SERVER_PORT).build(), (session, data) -> {
			System.out.println("--------------------------"+data.getProtocolId());
		});
	}

	@Test
	public void request() {
		websocketClient.sendMessage(new MessageContent(1000, LoginRequest.valueOf(100000).toByteArray()));
		LockSupport.park();
	}
}
