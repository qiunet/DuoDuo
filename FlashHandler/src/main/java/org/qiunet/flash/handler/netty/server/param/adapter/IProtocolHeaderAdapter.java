package org.qiunet.flash.handler.netty.server.param.adapter;

import io.netty.buffer.ByteBuf;
import org.qiunet.flash.handler.common.message.MessageContent;
import org.qiunet.flash.handler.context.header.IProtocolHeader;

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
	 * 使用
	 * @param in
	 * @return
	 */
	IProtocolHeader newHeader(ByteBuf in);
	/**
	 * 得到头的长度
	 * @return
	 */
	int getHeaderLength();
}
