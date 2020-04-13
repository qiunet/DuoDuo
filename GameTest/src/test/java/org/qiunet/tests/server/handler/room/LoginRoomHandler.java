package org.qiunet.tests.server.handler.room;

import org.qiunet.flash.handler.common.annotation.RequestHandler;
import org.qiunet.flash.handler.context.request.tcp.ITcpRequest;
import org.qiunet.flash.handler.handler.tcp.TcpProtobufHandler;
import org.qiunet.tests.proto.LoginRoomProto;
import org.qiunet.tests.server.startup.context.PlayerActor;

/**
 * Created by qiunet.
 * 17/12/9
 */
@RequestHandler(ID = 1003, desc = "房间服的登录")
public class LoginRoomHandler extends TcpProtobufHandler<PlayerActor, LoginRoomProto.LoginRoomRequest> {

	@Override
	public void handler(PlayerActor playerActor, ITcpRequest<LoginRoomProto.LoginRoomRequest> context) throws Exception {
		logger.info("LoginRoomHandler received message "+context.getRequestData());
		playerActor.sendResponse(1000003, LoginRoomProto.LoginRoomResponse.newBuilder().setRoomSize(context.getRequestData().getHeader().getUid()).build());
	}
}
