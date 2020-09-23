package org.qiunet.flash.handler.bootstrap;

import io.netty.util.CharsetUtil;
import org.junit.Assert;
import org.junit.Test;
import org.qiunet.flash.handler.common.message.MessageContent;
import org.qiunet.flash.handler.proto.LoginRequest;
import org.qiunet.flash.handler.proto.LoginResponse;
import org.qiunet.utils.protobuf.ProtobufDataManager;

/**
 * Created by qiunet.
 * 17/11/25
 */
public class TestTcpBootStrap extends TcpBootStrap {
	private String text;
	@Test
	public void testTcpString(){
		text = "测试 [testTcpString]";
		byte [] bytes = text.getBytes(CharsetUtil.UTF_8);
		MessageContent messageContent  = new MessageContent(1003, bytes);
		this.tcpClient.sendMessage(messageContent);
	}

	@Test
	public void testTcpProtobuf(){
		text = "test [testTcpProtobuf]";
		LoginRequest request = LoginRequest.valueOf(text, text, 11);
		MessageContent content = new MessageContent(1004, request.toByteArray());
		this.tcpClient.sendMessage(content);
	}

	@Override
	public void responseTcpMessage(MessageContent data) {
		switch (data.getProtocolId()) {
			case 2000:
				Assert.assertEquals(text, new String(data.bytes(), CharsetUtil.UTF_8));
				break;
			case 2001:
				LoginResponse response = ProtobufDataManager.decode(LoginResponse.class, data.bytes());
				Assert.assertEquals(text, response.getTestString());
				break;
		}
	}
}
