package org.qiunet.flash.handler.context.header;

import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;

import java.nio.ByteBuffer;

/***
 * 创建启动上下文
 * @author qiunet
 */
public interface IProtocolHeaderType {
	/***
	 * 使用bytes 和 protocolId 构造一个 ProtocolHeader
	 * @param bytes
	 * @return
	 */
	IProtocolHeader outHeader(int protocolId, ByteBuffer bytes);
	/***
	 * 使用ByteBuf
	 * @param in
	 * @return
	 */
	default IProtocolHeader inHeader(ByteBuf in) {
		return inHeader(in, null);
	}

	/**
	 * channel 加入. 可以解析时候保存一些信息.
	 * 比如防止回放攻击.
	 * @param in bytein
	 * @param channel channel
	 * @return
	 */
	IProtocolHeader inHeader(ByteBuf in, Channel channel);
	/**
	 * 得到请求头的长度
	 * @return
	 */
	int getReqHeaderLength();

	/***
	 * 得到响应头长度
	 * @return
	 */
	int getRspHeaderLength();

	/**
	 * 获得所有bytes数据
	 * @return
	 */
	default byte[] getAllBytes(int protocolId, ByteBuffer bytes){
		IProtocolHeader header = outHeader(protocolId, bytes);
		ByteBuffer allocate = ByteBuffer.allocate(getRspHeaderLength() + bytes.limit());
		allocate.put(header.dataBytes());
		allocate.put(bytes);
		return allocate.array();
	}
}
