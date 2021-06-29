package org.qiunet.flash.handler.bootstrap;

import org.junit.Assert;
import org.junit.Test;
import org.qiunet.flash.handler.common.message.MessageContent;
import org.qiunet.flash.handler.context.session.DSession;
import org.qiunet.flash.handler.proto.GenderType;
import org.qiunet.flash.handler.proto.LoginResponse;
import org.qiunet.flash.handler.proto.TcpPbLoginRequest;
import org.qiunet.utils.logger.LoggerType;
import org.qiunet.utils.protobuf.ProtobufDataManager;

/**
 * Created by qiunet.
 * 17/11/25
 */
public class TestTcpBootStrap extends TcpBootStrap {
	private String text;

	@Test
	public void testTcpProtobuf(){
		text = "test [testTcpProtobuf]";
		TcpPbLoginRequest request = TcpPbLoginRequest.valueOf(text, text, 11, GenderType.FAMALE);
		this.tcpClientConnector.sendMessage(request, true);
	}

	@Override
	public void responseTcpMessage(DSession session, MessageContent data) {
		// test 的地方.直接使用bytes 解析. 免得release
		LoginResponse response = ProtobufDataManager.decode(LoginResponse.class, data.bytes());
		LoggerType.DUODUO_FLASH_HANDLER.info("=TCP Response Text:[{}]" , response.getTestString());
		Assert.assertEquals(text, response.getTestString());
	}
}
