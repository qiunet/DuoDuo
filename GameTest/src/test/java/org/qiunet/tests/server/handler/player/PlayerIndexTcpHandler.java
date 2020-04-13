package org.qiunet.tests.server.handler.player;

import org.qiunet.flash.handler.common.annotation.RequestHandler;
import org.qiunet.flash.handler.context.request.websocket.IWebSocketRequest;
import org.qiunet.flash.handler.handler.websocket.WebSocketProtobufHandler;
import org.qiunet.tests.proto.PlayerIndexProto;
import org.qiunet.tests.server.startup.context.PlayerActor;

/**
 * Created by qiunet.
 * 17/12/9
 */
@RequestHandler(ID = 1002, desc = "长连接首页")
public class PlayerIndexTcpHandler extends WebSocketProtobufHandler<PlayerActor, PlayerIndexProto.PlayerIndexRequest> {

	@Override
	public void handler(PlayerActor playerActor, IWebSocketRequest<PlayerIndexProto.PlayerIndexRequest> context) throws Exception {
		playerActor.sendResponse(1000001, PlayerIndexProto.PlayerIndexResponse.newBuilder()
			.addItems(PlayerIndexProto.Item.newBuilder().setItemId(123450).setCount(context.getRequestData().getHeader().getUid()).build())
			.addItems(PlayerIndexProto.Item.newBuilder().setItemId(123451).setCount(context.getRequestData().getHeader().getUid()).build())
			.addItems(PlayerIndexProto.Item.newBuilder().setItemId(123452).setCount(context.getRequestData().getHeader().getUid()).build())
			.addItems(PlayerIndexProto.Item.newBuilder().setItemId(123453).setCount(context.getRequestData().getHeader().getUid()).build())
			.addItems(PlayerIndexProto.Item.newBuilder().setItemId(123454).setCount(context.getRequestData().getHeader().getUid()).build())
			.addItems(PlayerIndexProto.Item.newBuilder().setItemId(123455).setCount(context.getRequestData().getHeader().getUid()).build())
			.addItems(PlayerIndexProto.Item.newBuilder().setItemId(123456).setCount(context.getRequestData().getHeader().getUid()).build())
			.build());
	}
}
