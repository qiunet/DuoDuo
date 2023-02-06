package org.qiunet.function.gm.handler;

import org.qiunet.data.util.ServerConfig;
import org.qiunet.flash.handler.common.player.PlayerActor;
import org.qiunet.flash.handler.context.request.persistconn.IPersistConnRequest;
import org.qiunet.flash.handler.context.status.IGameStatus;
import org.qiunet.flash.handler.handler.persistconn.PersistConnPbHandler;
import org.qiunet.flash.handler.netty.server.config.adapter.message.StatusTipsRsp;
import org.qiunet.function.gm.proto.req.GmCommandReq;
import org.qiunet.function.gm.proto.rsp.GmCommandRsp;
import org.qiunet.utils.logger.LoggerType;

/***
 * 处理gm 请求
 *
 * @author qiunet
 * 2021-01-09 16:16
 */
public class GmCommandHandler extends PersistConnPbHandler<PlayerActor, GmCommandReq> {
	@Override
	public void handler(PlayerActor playerActor, IPersistConnRequest<GmCommandReq> context) throws Exception {
		if (ServerConfig.isOfficial()) {
			LoggerType.DUODUO.error("Current is official server. can not access gm command!");
			return;
		}

		GmCommandReq requestData = context.getRequestData();
		GmCommandManager.CommandHandlerMethodInfo commandInfo = GmCommandManager.instance.getCommandInfo(requestData.getType());
		if (commandInfo == null) {
			LoggerType.DUODUO.error("type [{}] of Command handler absent!", requestData.getType());
			return;
		}

		if (commandInfo.paramList.size() != requestData.getParams().size()) {
			LoggerType.DUODUO.error("type [{}] of Command request param size [{}] error!", requestData.getType(), requestData.getParams().size());
			return;
		}

		IGameStatus status = commandInfo.handler(playerActor, requestData.getParams());
		if (status.getStatus() != IGameStatus.SUCCESS.getStatus()) {
			playerActor.sendMessage(StatusTipsRsp.valueOf(status));
		}
		playerActor.sendMessage(GmCommandRsp.valueOf(status));
	}
}
