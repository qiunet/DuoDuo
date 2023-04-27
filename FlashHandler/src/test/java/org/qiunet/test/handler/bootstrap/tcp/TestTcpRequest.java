package org.qiunet.test.handler.bootstrap.tcp;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.qiunet.flash.handler.context.header.SequenceIdProtocolHeader;
import org.qiunet.flash.handler.context.request.data.IChannelData;
import org.qiunet.flash.handler.context.session.ClientSession;
import org.qiunet.flash.handler.netty.client.param.TcpClientConfig;
import org.qiunet.flash.handler.netty.client.tcp.NettyTcpClient;
import org.qiunet.flash.handler.netty.client.trigger.IClientTrigger;
import org.qiunet.flash.handler.netty.server.message.ConnectionReq;
import org.qiunet.test.handler.proto.GenderType;
import org.qiunet.test.handler.proto.LoginResponse;
import org.qiunet.test.handler.proto.TcpPbLoginRequest;
import org.qiunet.utils.logger.LoggerType;
import org.qiunet.utils.string.StringUtil;
import org.qiunet.utils.string.ToString;

import java.util.concurrent.locks.LockSupport;

/**
 * Created by qiunet.
 * 17/11/25
 */
public class TestTcpRequest extends BasicTcpBootStrap implements IClientTrigger {
	protected ClientSession tcpClientConnector;
	private String text;

	@Test
	public void testTcpProtobuf(){
		text = "test [testTcpProtobuf]";
		this.tcpClientConnector.sendMessage(ConnectionReq.valueOf(StringUtil.randomString(10)), true);

		TcpPbLoginRequest request = TcpPbLoginRequest.valueOf(text, text, 11, GenderType.FAMALE);
		this.tcpClientConnector.sendMessage(request, true, (status, rsp) -> {
			if (! status.isSuccess()) {
				// 不成功
				LoggerType.DUODUO_FLASH_HANDLER.error(ToString.toString(rsp));
				return;
			}
			LoginResponse response = (LoginResponse) rsp;
			// test 的地方.直接使用bytes 解析. 免得release
			LoggerType.DUODUO_FLASH_HANDLER.info("====Sequence header TCP Response Text:[{}]" , response.getTestString());
			Assertions.assertEquals(text, response.getTestString());
			LockSupport.unpark(currThread);
		});
	}

	@BeforeEach
	public void connect(){
		currThread = Thread.currentThread();
		NettyTcpClient tcpClient = NettyTcpClient.create(TcpClientConfig.custom().setProtocolHeader(SequenceIdProtocolHeader.instance).build(), this);
		tcpClientConnector = tcpClient.connect(host, port);
	}
	@AfterEach
	public void closeConnect(){
		currThread = Thread.currentThread();
		LockSupport.park();
	}

	@Override
	public void response(ClientSession session, IChannelData response) {
		LoggerType.DUODUO_FLASH_HANDLER.info("=TCP Response ID:[{}]" , response.protocolId());
	}
}
