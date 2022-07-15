package org.qiunet.function.gm.handler;

import org.qiunet.data.util.ServerConfig;
import org.qiunet.flash.handler.common.player.AbstractMessageActor;
import org.qiunet.flash.handler.common.player.AbstractUserActor;
import org.qiunet.flash.handler.common.player.PlayerActor;
import org.qiunet.flash.handler.common.player.UserOnlineManager;
import org.qiunet.flash.handler.context.request.persistconn.IPersistConnRequest;
import org.qiunet.flash.handler.handler.persistconn.PersistConnPbHandler;
import org.qiunet.function.gm.proto.req.GmOnlineUserReq;
import org.qiunet.function.gm.proto.rsp.GmOnlineUserRsp;
import org.qiunet.function.gm.proto.rsp.OnlineUserInfo;
import org.qiunet.utils.collection.enums.ForEachResult;
import org.qiunet.utils.logger.LoggerType;

import java.util.ArrayList;
import java.util.List;

/***
 * gm 命令首页
 *
 * @author qiunet
 * 2021-01-09 16:03
 */
public class GmOnlineUserHandler extends PersistConnPbHandler<AbstractMessageActor, GmOnlineUserReq> {
	@Override
	public void handler(AbstractMessageActor messageActor, IPersistConnRequest<GmOnlineUserReq> context) throws Exception {
		if (ServerConfig.isOfficial()) {
			LoggerType.DUODUO.error("Current is official server. can not access gm command!");
			return;
		}
		List<OnlineUserInfo> list = new ArrayList<>();
		UserOnlineManager.instance.foreach(actor -> {
			PlayerActor playerActor = (PlayerActor) actor;
			list.add(OnlineUserInfo.valueOf(playerActor.getOpenId(), actor.getId()));
			return ForEachResult.CONTINUE;
		}, AbstractUserActor::isPlayerActor);
		messageActor.sendMessage(GmOnlineUserRsp.valueOf(list));
	}

	@Override
	public boolean needAuth() {
		return false;
	}
}
