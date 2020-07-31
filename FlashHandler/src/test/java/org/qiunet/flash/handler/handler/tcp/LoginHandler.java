package org.qiunet.flash.handler.handler.tcp;

import org.qiunet.flash.handler.common.annotation.RequestHandler;
import org.qiunet.flash.handler.context.request.tcp.ITcpRequest;
import org.qiunet.flash.handler.startup.context.PlayerActor;

/**
 * Created by qiunet.
 * 17/11/21
 */
@RequestHandler(ID = 1003, desc = "测试")
public class LoginHandler extends TcpStringHandler<PlayerActor> {

	@Override
	public void handler(PlayerActor playerActor, ITcpRequest<String> context) throws Exception {
		playerActor.sendResponse(2000, context.getRequestData());
	}
}
