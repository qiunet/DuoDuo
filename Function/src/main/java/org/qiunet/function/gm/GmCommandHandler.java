package org.qiunet.function.gm;

import org.qiunet.flash.handler.common.player.AbstractPlayerActor;
import org.qiunet.flash.handler.context.request.persistconn.IPersistConnRequest;
import org.qiunet.flash.handler.context.status.IGameStatus;
import org.qiunet.flash.handler.handler.persistconn.PersistConnPbHandler;
import org.qiunet.function.gm.message.req.GmCommandReq;
import org.qiunet.function.gm.message.resp.GmCommandResp;
import org.qiunet.utils.logger.LoggerType;

/***
 * 处理gm 请求
 *
 * @author qiunet
 * 2021-01-09 16:16
 */
public class GmCommandHandler extends PersistConnPbHandler<AbstractPlayerActor, GmCommandReq> {
	@Override
	public void handler(AbstractPlayerActor playerActor, IPersistConnRequest<GmCommandReq> context) throws Exception {
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
		playerActor.sendMessage(GmCommandResp.valueOf(status));
	}
}
