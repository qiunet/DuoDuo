package org.qiunet.flash.handler.handler.WebSocket;

import org.qiunet.flash.handler.common.annotation.RequestHandler;
import org.qiunet.flash.handler.context.request.tcp.ITcpRequest;
import org.qiunet.flash.handler.context.request.websocket.IWebSocketRequest;
import org.qiunet.flash.handler.handler.tcp.TcpStringHandler;
import org.qiunet.flash.handler.handler.websocket.WebSocketStringHandler;

/**
 * Created by qiunet.
 * 17/11/21
 */
@RequestHandler(ID = 1005, desc = "测试")
public class LoginHandler extends WebSocketStringHandler {

	@Override
	public void handler(IWebSocketRequest<String> context)throws Exception {
		context.response(2000, context.getRequestData());
	}
}
