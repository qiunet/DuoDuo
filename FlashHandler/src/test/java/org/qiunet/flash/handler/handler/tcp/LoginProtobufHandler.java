package org.qiunet.flash.handler.handler.tcp;

import org.qiunet.flash.handler.common.annotation.RequestHandler;
import org.qiunet.flash.handler.context.request.tcp.ITcpRequest;
import org.qiunet.flash.handler.proto.LoginResponse;
import org.qiunet.flash.handler.proto.TcpPbLoginRequest;
import org.qiunet.flash.handler.startup.context.PlayerActor;

/**
 * Created by qiunet.
 * 17/8/16
 */
@RequestHandler(ID = 3001, desc = "tcp protobuf 测试")
public class LoginProtobufHandler extends TcpProtobufHandler<PlayerActor, TcpPbLoginRequest> {

	@Override
	public void handler(PlayerActor player, ITcpRequest<TcpPbLoginRequest> context)throws Exception {
		player.sendResponse(LoginResponse.valueOf(context.getRequestData().getAccount()));
	}
}
