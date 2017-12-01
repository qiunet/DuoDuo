package org.qiunet.flash.handler.netty.client.websocket;

import io.netty.handler.codec.http.websocketx.WebSocketFrame;

/**
 * Created by qiunet.
 * 17/12/1
 */
public interface IWebsocketResponseTrigger {
	/***
	 * response
	 * @param webSocketFrame
	 */
	void response(WebSocketFrame webSocketFrame);
}
