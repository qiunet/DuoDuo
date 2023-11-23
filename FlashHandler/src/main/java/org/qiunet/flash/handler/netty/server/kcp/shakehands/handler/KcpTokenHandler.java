package org.qiunet.flash.handler.netty.server.kcp.shakehands.handler;

import org.qiunet.flash.handler.common.enums.ServerConnType;
import org.qiunet.flash.handler.common.player.PlayerActor;
import org.qiunet.flash.handler.context.request.persistconn.IPersistConnRequest;
import org.qiunet.flash.handler.context.status.StatusResult;
import org.qiunet.flash.handler.context.status.StatusResultException;
import org.qiunet.flash.handler.handler.persistconn.PersistConnPbHandler;
import org.qiunet.flash.handler.netty.server.constants.ServerConstants;
import org.qiunet.flash.handler.netty.server.kcp.shakehands.mapping.KcpPlayerTokenMapping;
import org.qiunet.flash.handler.netty.server.kcp.shakehands.message.KcpTokenReq;
import org.qiunet.flash.handler.netty.server.kcp.shakehands.message.KcpTokenRsp;

/***
 *
 * @author qiunet
 * 2022/4/27 11:37
 */
public class KcpTokenHandler extends PersistConnPbHandler<PlayerActor, KcpTokenReq> {
	@Override
	public void handler(PlayerActor playerActor, IPersistConnRequest<KcpTokenReq> context) throws Exception {
		ServerConnType type = playerActor.getSession().getAttachObj(ServerConstants.HANDLER_TYPE_KEY);
		if (type != ServerConnType.TCP && type != ServerConnType.WS) {
			throw StatusResultException.valueOf(StatusResult.FAIL);
		}

		KcpPlayerTokenMapping info = KcpPlayerTokenMapping.get(playerActor);
		if (info == null) {
			info = new KcpPlayerTokenMapping(playerActor);
		}
		KcpTokenRsp kcpTokenRsp = KcpTokenRsp.valueOf(info.getConvId(), info.getToken(), info.getPort());
		playerActor.sendMessage(kcpTokenRsp);
	}
}
