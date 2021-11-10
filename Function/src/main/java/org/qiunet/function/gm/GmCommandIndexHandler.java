package org.qiunet.function.gm;

import org.qiunet.data.util.ServerConfig;
import org.qiunet.flash.handler.common.player.AbstractPlayerActor;
import org.qiunet.flash.handler.context.request.persistconn.IPersistConnRequest;
import org.qiunet.flash.handler.handler.persistconn.PersistConnPbHandler;
import org.qiunet.function.gm.message.req.GmCommandIndexReq;
import org.qiunet.function.gm.message.resp.GmCommandIndexResp;
import org.qiunet.utils.logger.LoggerType;

/***
 * gm 命令首页
 *
 * @author qiunet
 * 2021-01-09 16:03
 */
public class GmCommandIndexHandler extends PersistConnPbHandler<AbstractPlayerActor, GmCommandIndexReq> {
	@Override
	public void handler(AbstractPlayerActor playerActor, IPersistConnRequest<GmCommandIndexReq> context) throws Exception {
		if (ServerConfig.isOfficial()) {
			LoggerType.DUODUO.error("Current is official server. can not access gm command!");
			return;
		}

		playerActor.sendMessage(GmCommandIndexResp.valueOf(GmCommandManager.instance.getInfoList()));
	}
}
