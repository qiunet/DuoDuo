package org.qiunet.flash.handler.context.header;

import org.qiunet.flash.handler.context.request.data.IChannelData;

/***
 * 有序列号的协议头
 * 如果需要响应序号 需要调用下面API才会设置 sequence
 * @see org.qiunet.flash.handler.context.request.persistconn.IPersistConnRequest#sendMessage(IChannelData)
 * @see org.qiunet.flash.handler.context.request.persistconn.IPersistConnRequest#sendMessage(IChannelData, boolean)
 *
 * @author qiunet
 * 2023/4/26 11:28
 */
public interface ISequenceProtocolHeader {
	String MESSAGE_KEY = "sequence";
	/**
	 * 序列号
	 * @return 序列号
	 */
	int sequence();
}
