package org.qiunet.flash.handler.bootstrap;

import com.google.protobuf.InvalidProtocolBufferException;
import io.netty.util.CharsetUtil;
import org.junit.Assert;
import org.junit.Test;
import org.qiunet.flash.handler.common.message.MessageContent;
import org.qiunet.flash.handler.handler.proto.LoginProto;

/***
 *
 * @Author qiunet
 * @Date Create in 2018/10/10 11:39
 **/
public class TestUdpBootStrap extends UdpBootStrap {
	private String text;
	@Test
	public void testUdpString(){
		text = "测试 [testUdpString]";
		byte [] bytes = text.getBytes(CharsetUtil.UTF_8);
		MessageContent messageContent  = new MessageContent(1008, bytes);
		this.udpClient.sendMessage(messageContent);
	}

	@Test
	public void testUdpProtobuf(){
		text = "test [testUdpProtobuf]";
		LoginProto.LoginRequest request = LoginProto.LoginRequest.newBuilder().setTestString(text).build();
		MessageContent content = new MessageContent(1009, request.toByteArray());
		this.udpClient.sendMessage(content);
	}

	@Override
	protected void responseUdpMessage(MessageContent data) {
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
