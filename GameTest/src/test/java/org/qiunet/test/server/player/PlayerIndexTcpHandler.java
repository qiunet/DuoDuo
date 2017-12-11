package org.qiunet.test.server.player;

import org.qiunet.flash.handler.common.annotation.RequestHandler;
import org.qiunet.flash.handler.context.request.http.IHttpRequest;
import org.qiunet.flash.handler.context.request.tcp.ITcpRequest;
import org.qiunet.flash.handler.handler.http.HttpProtobufHandler;
import org.qiunet.flash.handler.handler.tcp.TcpProtobufHandler;
import org.qiunet.test.proto.PlayerIndexProto;

/**
 * Created by qiunet.
 * 17/12/9
 */
@RequestHandler(ID = 1002, desc = "长连接首页")
public class PlayerIndexTcpHandler extends TcpProtobufHandler<PlayerIndexProto.PlayerIndexRequest> {

	@Override
	public void handler(ITcpRequest<PlayerIndexProto.PlayerIndexRequest> context) {
		context.response(1000001, PlayerIndexProto.PlayerIndexResponse.newBuilder()
				.addItems(PlayerIndexProto.Item.newBuilder().setItemId(123450).setCount(20).build())
				.addItems(PlayerIndexProto.Item.newBuilder().setItemId(123451).setCount(21).build())
				.addItems(PlayerIndexProto.Item.newBuilder().setItemId(123452).setCount(22).build())
				.addItems(PlayerIndexProto.Item.newBuilder().setItemId(123453).setCount(23).build())
				.addItems(PlayerIndexProto.Item.newBuilder().setItemId(123454).setCount(24).build())
				.addItems(PlayerIndexProto.Item.newBuilder().setItemId(123455).setCount(25).build())
				.addItems(PlayerIndexProto.Item.newBuilder().setItemId(123456).setCount(26).build())
				.build());
	}
}
