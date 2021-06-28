package org.qiunet.flash.handler.netty.client;

import org.qiunet.flash.handler.common.message.MessageContent;
import org.qiunet.flash.handler.context.sender.IChannelMessageSender;
import org.qiunet.flash.handler.context.session.future.IDSessionFuture;

/**
 * 长连接客户端
 * Created by qiunet.
 * 17/12/8
 */
public interface IPersistConnClient extends IChannelMessageSender {
	/***
	 * 直接发送消息
	 * client 没办法得到所有的protocolId
	 * @param content
	 */
	IDSessionFuture sendMessage(MessageContent content);
}
