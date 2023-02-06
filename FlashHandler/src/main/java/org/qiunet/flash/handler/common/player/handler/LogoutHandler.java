package org.qiunet.flash.handler.common.player.handler;

import org.qiunet.flash.handler.common.player.PlayerActor;
import org.qiunet.flash.handler.common.player.proto.LogoutReq;
import org.qiunet.flash.handler.context.request.persistconn.IPersistConnRequest;
import org.qiunet.flash.handler.handler.persistconn.PersistConnPbHandler;

/***
 * 登出处理
 * @author qiunet
 * 2023/2/6 20:52
 */
public class LogoutHandler extends PersistConnPbHandler<PlayerActor, LogoutReq> {

	@Override
	public void handler(PlayerActor playerActor, IPersistConnRequest<LogoutReq> context) throws Exception {
		playerActor.logout();
	}
}
