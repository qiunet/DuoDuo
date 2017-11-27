package org.qiunet.flash.handler.bootstrap;

import io.netty.util.CharsetUtil;
import org.junit.Test;
import org.qiunet.flash.handler.context.header.MessageContent;

/**
 * Created by qiunet.
 * 17/11/25
 */
public class TestTcpBootStrap extends TcpBootStrap {
	@Test
	public void testTcpString(){
		String test = "测试 [testTcpString]";
		System.out.println(test);
		byte [] bytes = test.getBytes(CharsetUtil.UTF_8);
		MessageContent messageContent  = new MessageContent(1003, bytes);
		this.tcpClient.sendTcpMessage(messageContent);
	}

	@Test
	public void testTcpProtobuf(){
		String test = "测试 [testTcpProtobuf]";
	}

	@Override
	public void responseTcpMessage(MessageContent data) {
		System.out.println("========="+data.bytes().toString());
	}
}
