package org.qiunet.flash.handler.netty.client.websocket;

import org.qiunet.flash.handler.context.header.MessageContent;

/**
 * Created by qiunet.
 * 17/12/1
 */
public interface IWebsocketResponseTrigger {
	/***
	 * response
	 * @param data
	 */
	void response(MessageContent data);
}
