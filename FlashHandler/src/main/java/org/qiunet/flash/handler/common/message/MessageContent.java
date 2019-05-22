package org.qiunet.flash.handler.common.message;

import io.netty.buffer.ByteBuf;
import org.qiunet.flash.handler.context.header.DefaultProtocolHeader;
import org.qiunet.flash.handler.netty.bytebuf.PooledBytebufFactory;

/**
 *  上下行消息的封装类.
 *  netty 只跟byte数组打交道.
 *  其它自行解析
 * @author qiunet
 *         Created on 17/3/13 19:50.
 */
public class MessageContent {
	protected byte [] bytes;
	protected int protocolId;

	public MessageContent(int protocolId, byte [] bytes) {
		this.bytes = bytes;
		this.protocolId = protocolId;
	}

	public int getProtocolId() {
		return protocolId;
	}

	public byte [] bytes() {
		return bytes;
	}

	/***
	 * 把header信息也encode 进去. 返回bytebuf
	 *
	 * 业务不要调用这个方法.
	 *
	 * @return
	 */
	public ByteBuf encodeToByteBuf(){
		DefaultProtocolHeader header = new DefaultProtocolHeader(bytes, protocolId);
		ByteBuf byteBuf = PooledBytebufFactory.getInstance().alloc(bytes.length + header.getHeaderLength());
		header.writeToByteBuf(byteBuf);
		byteBuf.writeBytes(bytes);
		return byteBuf;
	}
}
