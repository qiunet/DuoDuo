package org.qiunet.flash.handler.netty.coder;

import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import org.qiunet.flash.handler.context.header.IProtocolHeader;

/***
 *
 * @author qiunet
 * 2022/10/20 17:45
 */
public interface IMessageDecoder {

	/**
	 * 获得头的长度
	 * @param protocolHeader protocolHeader 实例
	 * @param connectReq 是否第一次连接
	 * @return 头的长度
	 */
	default int getHeaderLength(IProtocolHeader protocolHeader, boolean connectReq) {
		return protocolHeader.getServerInHeadLength(connectReq);
	}

	/**
	 * 获得对应的头对象
	 * @param protocolHeader protocolHeader 实例
	 * @param in 消息数据
	 * @param channel channel实例
	 * @param connectReq 是否第一次连接
	 * @return ProtocolHeader 实例
	 */
	default IProtocolHeader.ProtocolHeader getHeader(IProtocolHeader protocolHeader, ByteBuf in, Channel channel, boolean connectReq) {
		return protocolHeader.getServerInHeader(connectReq, in, channel);
	}
}
