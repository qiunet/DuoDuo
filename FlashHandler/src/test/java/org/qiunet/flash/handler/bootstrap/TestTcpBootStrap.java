package org.qiunet.flash.handler.bootstrap;

import io.netty.util.CharsetUtil;
import org.junit.Assert;
import org.junit.Test;
import org.qiunet.flash.handler.common.message.MessageContent;
import org.qiunet.flash.handler.context.session.DSession;
import org.qiunet.flash.handler.proto.LoginResponse;
import org.qiunet.flash.handler.proto.TcpPbLoginRequest;
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
		TcpPbLoginRequest request = TcpPbLoginRequest.valueOf(text, text, 11);
		MessageContent content = new MessageContent(3001, request.toByteArray());
		this.tcpClient.sendMessage(content);
	}

	@Override
	public void responseTcpMessage(DSession session, MessageContent data) {
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
