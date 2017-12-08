package org.qiunet.flash.handler.netty.client;

import org.qiunet.flash.handler.context.header.MessageContent;

/**
 * 长连接客户端
 * Created by qiunet.
 * 17/12/8
 */
public interface ILongConnClient {
	/***
	 * 发送消息
	 * @param content
	 */
	void sendMessage(MessageContent content);

	/***
	 * 关闭
	 */
	void close();
}
