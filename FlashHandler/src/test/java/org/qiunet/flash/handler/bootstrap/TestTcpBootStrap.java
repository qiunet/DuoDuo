package org.qiunet.flash.handler.bootstrap;

import com.google.protobuf.InvalidProtocolBufferException;
import io.netty.util.CharsetUtil;
import org.junit.Assert;
import org.junit.Test;
import org.qiunet.flash.handler.context.header.MessageContent;
import org.qiunet.flash.handler.handler.proto.LoginProto;

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
		this.tcpClient.sendTcpMessage(messageContent);
	}

	@Test
	public void testTcpProtobuf(){
		text = "test [testTcpProtobuf]";
		LoginProto.LoginRequest request = LoginProto.LoginRequest.newBuilder().setTestString(text).build();
		MessageContent content = new MessageContent(1004, request.toByteArray());
		this.tcpClient.sendTcpMessage(content);
	}

	@Override
	public void responseTcpMessage(MessageContent data) {
		switch (data.getProtocolId()) {
			case 2000:
				Assert.assertEquals(text, new String(data.bytes(), CharsetUtil.UTF_8));
				break;
			case 2001:
				LoginProto.LoginResponse response = null;
				try {
					response = LoginProto.LoginResponse.parseFrom(data.bytes());
				} catch (InvalidProtocolBufferException e) {
					e.printStackTrace();
				}
				Assert.assertEquals(text, response.getTestString());
				break;
		}
	}
}
