package org.qiunet.function.gm.handler;

import org.qiunet.data.conf.ServerConfig;
import org.qiunet.flash.handler.common.player.AbstractMessageActor;
import org.qiunet.flash.handler.context.request.persistconn.IPersistConnRequest;
import org.qiunet.flash.handler.handler.persistconn.PersistConnPbHandler;
import org.qiunet.function.gm.proto.req.GmCommandIndexReq;
import org.qiunet.function.gm.proto.rsp.GmCommandIndexRsp;
import org.qiunet.utils.logger.LoggerType;

/***
 * gm 命令首页
 *
 * @author qiunet
 * 2021-01-09 16:03
 */
public class GmCommandIndexHandler extends PersistConnPbHandler<AbstractMessageActor, GmCommandIndexReq> {
	@Override
	public void handler(AbstractMessageActor playerActor, IPersistConnRequest<GmCommandIndexReq> context) throws Exception {
		if (ServerConfig.isOfficial()) {
			LoggerType.DUODUO.error("Current is official server. can not access gm command!");
			return;
		}

		playerActor.sendMessage(GmCommandIndexRsp.valueOf(GmCommandManager.instance.getInfoList()));
	}

	@Override
	public boolean needAuth() {
		return false;
	}
}
