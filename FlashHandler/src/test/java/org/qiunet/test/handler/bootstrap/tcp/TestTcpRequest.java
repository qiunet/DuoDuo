package org.qiunet.test.handler.bootstrap.tcp;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.qiunet.flash.handler.common.message.MessageContent;
import org.qiunet.flash.handler.common.protobuf.ProtobufDataManager;
import org.qiunet.flash.handler.context.session.ISession;
import org.qiunet.flash.handler.netty.client.param.TcpClientParams;
import org.qiunet.flash.handler.netty.client.tcp.NettyTcpClient;
import org.qiunet.flash.handler.netty.client.tcp.TcpClientConnector;
import org.qiunet.flash.handler.netty.client.trigger.IPersistConnResponseTrigger;
import org.qiunet.test.handler.proto.GenderType;
import org.qiunet.test.handler.proto.LoginResponse;
import org.qiunet.test.handler.proto.TcpPbLoginRequest;
import org.qiunet.utils.logger.LoggerType;

import java.util.concurrent.locks.LockSupport;

/**
 * Created by qiunet.
 * 17/11/25
 */
public class TestTcpRequest extends BasicTcpBootStrap implements IPersistConnResponseTrigger {
	protected TcpClientConnector tcpClientConnector;
	private String text;

	@Test
	public void testTcpProtobuf(){
		text = "test [testTcpProtobuf]";
		TcpPbLoginRequest request = TcpPbLoginRequest.valueOf(text, text, 11, GenderType.FAMALE);
		this.tcpClientConnector.sendMessage(request, true);
	}

	@BeforeEach
	public void connect(){
		currThread = Thread.currentThread();
		NettyTcpClient tcpClient = NettyTcpClient.create(TcpClientParams.DEFAULT_PARAMS, this);
		tcpClientConnector = tcpClient.connect(host, port);
	}
	@AfterEach
	public void closeConnect(){
		currThread = Thread.currentThread();
		LockSupport.park();
	}

	@Override
	public void response(ISession session, MessageContent data) {
		// test 的地方.直接使用bytes 解析. 免得release
		LoginResponse response = ProtobufDataManager.decode(LoginResponse.class, data.bytes());
		LoggerType.DUODUO_FLASH_HANDLER.info("=TCP Response Text:[{}]" , response.getTestString());
		Assertions.assertEquals(text, response.getTestString());
		LockSupport.unpark(currThread);
	}
}
