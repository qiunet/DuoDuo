package org.qiunet.flash.handler.netty.server.kcp.shakehands.handler;

import io.netty.util.Attribute;
import org.qiunet.data.util.ServerConfig;
import org.qiunet.flash.handler.common.enums.ServerConnType;
import org.qiunet.flash.handler.common.player.PlayerActor;
import org.qiunet.flash.handler.context.request.persistconn.IPersistConnRequest;
import org.qiunet.flash.handler.context.status.StatusResult;
import org.qiunet.flash.handler.context.status.StatusResultException;
import org.qiunet.flash.handler.handler.persistconn.PersistConnPbHandler;
import org.qiunet.flash.handler.netty.server.constants.ServerConstants;
import org.qiunet.flash.handler.netty.server.kcp.shakehands.mapping.KcpPlayerTokenMapping;
import org.qiunet.flash.handler.netty.server.kcp.shakehands.message.KcpTokenRsp;

/***
 *
 * @author qiunet
 * 2022/4/27 11:37
 */
public class KcpTokenHandler extends PersistConnPbHandler<PlayerActor, KcpTokenRsp> {
	@Override
	public void handler(PlayerActor playerActor, IPersistConnRequest<KcpTokenRsp> context) throws Exception {
		Attribute<ServerConnType> attr = context.channel().attr(ServerConstants.HANDLER_TYPE_KEY);
		if (attr.get() != ServerConnType.TCP && attr.get() != ServerConnType.WS) {
			throw StatusResultException.valueOf(StatusResult.FAIL);
		}

		String token = KcpPlayerTokenMapping.mapping(playerActor.getPlayerId());
		KcpTokenRsp kcpTokenRsp = KcpTokenRsp.valueOf(token, ServerConfig.getServerPort());
		playerActor.sendMessage(kcpTokenRsp);
	}
}
