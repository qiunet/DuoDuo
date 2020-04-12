package org.qiunet.flash.handler.handler.tcp;

import org.qiunet.flash.handler.common.annotation.RequestHandler;
import org.qiunet.flash.handler.context.request.tcp.ITcpRequest;
import org.qiunet.flash.handler.handler.proto.LoginProto;
import org.qiunet.flash.handler.startup.context.PlayerActor;

/**
 * Created by qiunet.
 * 17/8/16
 */
@RequestHandler(ID = 1004, desc = "protobuf 测试")
public class LoginProtobufHandler extends TcpProtobufHandler<PlayerActor, LoginProto.LoginRequest> {

	@Override
	public void handler(PlayerActor player, ITcpRequest<LoginProto.LoginRequest> context)throws Exception {
		player.sendResponse(2001, LoginProto.LoginResponse.newBuilder().setTestString(context.getRequestData().getTestString()).build());
	}
}
