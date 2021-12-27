package org.qiunet.flash.handler.context.header;

import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import org.qiunet.flash.handler.common.message.MessageContent;

import java.nio.ByteBuffer;

/***
 * 创建启动上下文
 * @author qiunet
 */
public interface IProtocolHeaderType {
	/***
	 * 使用bytes 和 protocolId 构造一个 ProtocolHeader
	 * @param content
	 * @return
	 */
	IProtocolHeader outHeader(MessageContent content);
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
	default byte[] getAllBytes(MessageContent content){
		IProtocolHeader header = outHeader(content);
		byte[] bytes = header.dataBytes();
		ByteBuffer allocate = ByteBuffer.allocate(getRspHeaderLength() + content.bytes().length);
		allocate.put(bytes);
		allocate.put(content.bytes());
		return allocate.array();
	}
}
