package org.qiunet.test.handler.bootstrap.kcp;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.qiunet.flash.handler.common.message.MessageContent;
import org.qiunet.flash.handler.common.protobuf.ProtobufDataManager;
import org.qiunet.flash.handler.context.session.ISession;
import org.qiunet.flash.handler.netty.client.kcp.NettyKcpClient;
import org.qiunet.flash.handler.netty.client.param.KcpClientParams;
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
public class TestKcpRequest extends BasicKcpBootStrap implements IPersistConnResponseTrigger {
	protected ISession session;
	private String text;

	@Test
	public void testKcpProtobuf(){
		text = "test [testTcpProtobuf]";
		TcpPbLoginRequest request = TcpPbLoginRequest.valueOf(text, text, 11, GenderType.FAMALE);
		this.session.sendMessage(request);
	}

	@BeforeEach
	public void connect(){
		currThread = Thread.currentThread();
		NettyKcpClient tcpClient = NettyKcpClient.create(KcpClientParams.DEFAULT_PARAMS, this);
		this.session = tcpClient.connect(host, port);
	}
	@AfterEach
	public void closeConnect(){
		currThread = Thread.currentThread();
		LockSupport.park();
	}

	@Override
	public void response(ISession session, MessageContent data) {
		// test 的地方.直接使用bytes 解析. 免得release
		LoginResponse response = ProtobufDataManager.decode(LoginResponse.class, data.byteBuffer());
		LoggerType.DUODUO_FLASH_HANDLER.info("=TCP Response Text:[{}]" , response.getTestString());
		Assertions.assertEquals(text, response.getTestString());
		LockSupport.unpark(currThread);
	}

}
