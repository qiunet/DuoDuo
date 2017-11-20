package org.qiunet.flash.handler.header;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import org.junit.Assert;
import org.junit.Test;
import org.qiunet.flash.handler.context.header.ProtocolHeader;

import java.util.Arrays;

/**
 * Created by qiunet.
 * 17/11/20
 */
public class TestProtocolHeader {
	@Test
	public void testheader(){
		ByteBuf byteBuf = Unpooled.buffer();
		ProtocolHeader header1 = new ProtocolHeader(100, 1000034, 1, 1314, 44664323);
		header1.writeToByteBuf(byteBuf);

		ProtocolHeader header2 = new ProtocolHeader(byteBuf);
		Assert.assertArrayEquals(header1.getMagic(), header2.getMagic());
		Assert.assertEquals(header1.getLength(), header2.getLength());
		Assert.assertEquals(header1.getChunkSize(), header2.getChunkSize());
		Assert.assertEquals(header1.getSequence(), header2.getSequence());
		Assert.assertEquals(header1.getProtocolId(), header2.getProtocolId());
		Assert.assertTrue(header2.crcIsValid(44664323L));
	}
}
