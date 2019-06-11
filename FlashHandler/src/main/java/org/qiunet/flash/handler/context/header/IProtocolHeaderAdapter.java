package org.qiunet.flash.handler.context.header;

import io.netty.buffer.ByteBuf;
import org.qiunet.flash.handler.common.message.MessageContent;

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
