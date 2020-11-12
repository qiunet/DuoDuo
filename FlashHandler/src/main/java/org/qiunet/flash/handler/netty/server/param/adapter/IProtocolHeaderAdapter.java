package org.qiunet.flash.handler.netty.server.param.adapter;

import io.netty.buffer.ByteBuf;
import org.qiunet.flash.handler.common.message.MessageContent;
import org.qiunet.flash.handler.context.header.IProtocolHeader;

import java.nio.ByteBuffer;

/***
 * 创建启动上下文
 * @author qiunet
 */
public interface IProtocolHeaderAdapter {
	/***
	 * 使用bytes 和 protocolId 构造一个 ProtocolHeader
	 * @param content
	 * @return
	 */
	IProtocolHeader newHeader(MessageContent content);
	/***
	 * 使用ByteBuf
	 * @param in
	 * @return
	 */
	IProtocolHeader newHeader(ByteBuf in);
	/***
	 * 使用ByteBuffer
	 * @param in
	 * @return
	 */
	IProtocolHeader newHeader(ByteBuffer in);
	/**
	 * 得到头的长度
	 * @return
	 */
	int getHeaderLength();

	/**
	 * 获得所有bytes数据
	 * @return
	 */
	default byte[] getAllBytes(MessageContent content){
		IProtocolHeader header = newHeader(content);
		byte[] bytes = header.dataBytes();
		ByteBuffer allocate = ByteBuffer.allocate(getHeaderLength() + content.bytes().length);
		allocate.put(bytes);
		allocate.put(content.bytes());
		return allocate.array();
	}
}
