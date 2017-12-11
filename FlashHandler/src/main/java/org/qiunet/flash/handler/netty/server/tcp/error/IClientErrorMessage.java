package org.qiunet.flash.handler.netty.server.tcp.error;

import org.qiunet.flash.handler.common.message.MessageContent;

/**
 * 对错误进行封装
 * Created by qiunet.
 * 17/11/26
 */
public interface IClientErrorMessage {
	/***
	 *  没有找到handler
	 * @return
	 */
	MessageContent getHandlerNotFound();
}
