package org.qiunet.test.server.handler.player;

import org.qiunet.flash.handler.common.annotation.RequestHandler;
import org.qiunet.flash.handler.context.request.websocket.IWebSocketRequest;
import org.qiunet.flash.handler.handler.websocket.WebSocketProtobufHandler;
import org.qiunet.test.proto.PlayerIndexProto;

/**
 * Created by qiunet.
 * 17/12/9
 */
@RequestHandler(ID = 1002, desc = "长连接首页")
public class PlayerIndexTcpHandler extends WebSocketProtobufHandler<PlayerIndexProto.PlayerIndexRequest> {

	@Override
	public void handler(IWebSocketRequest<PlayerIndexProto.PlayerIndexRequest> context) throws Exception {
		context.response(1000001, PlayerIndexProto.PlayerIndexResponse.newBuilder()
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
