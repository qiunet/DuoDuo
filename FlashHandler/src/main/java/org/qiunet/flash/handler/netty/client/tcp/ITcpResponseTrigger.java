package org.qiunet.flash.handler.netty.client.tcp;

import io.netty.buffer.ByteBuf;
import org.qiunet.flash.handler.context.header.MessageContent;

/**
 * Created by qiunet.
 * 17/11/25
 */
public interface ITcpResponseTrigger {
	/***
	 * 触发的响应
	 * @param data
	 */
	public void response(MessageContent data);
}
