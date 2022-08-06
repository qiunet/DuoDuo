package org.qiunet.function.gm.handler;

import io.netty.channel.Channel;
import org.qiunet.cross.node.ServerNode;
import org.qiunet.data.util.ServerConfig;
import org.qiunet.flash.handler.common.id.IProtocolId;
import org.qiunet.flash.handler.common.player.PlayerActor;
import org.qiunet.flash.handler.common.player.UserOnlineManager;
import org.qiunet.flash.handler.context.request.data.ChannelDataMapping;
import org.qiunet.flash.handler.context.request.persistconn.IPersistConnRequest;
import org.qiunet.flash.handler.context.status.IGameStatus;
import org.qiunet.flash.handler.context.status.StatusResultException;
import org.qiunet.flash.handler.handler.persistconn.PersistConnPbHandler;
import org.qiunet.function.gm.proto.req.GmCommandReq;
import org.qiunet.function.gm.proto.req.GmDToolsCommandReq;
import org.qiunet.function.gm.proto.rsp.GmDToolsCommandRsp;
import org.qiunet.utils.logger.LoggerType;

/***
 * 处理gm 请求
 *
 * @author qiunet
 * 2021-01-09 16:16
 */
public class GmDToolsCommandHandler extends PersistConnPbHandler<ServerNode, GmDToolsCommandReq> {
	@Override
	public boolean needAuth() {
		return false;
	}

	@Override
	public void handler(ServerNode actor, IPersistConnRequest<GmDToolsCommandReq> context) throws Exception {
		if (ServerConfig.isOfficial()) {
			throw StatusResultException.valueOf(IGameStatus.FAIL);
		}
		PlayerActor playerActor = UserOnlineManager.instance.getPlayerActor(context.getRequestData().getPlayerId());
		if (playerActor == null) {
			throw StatusResultException.valueOf(IGameStatus.FAIL);
		}

		PersistConnPbHandler<PlayerActor, GmCommandReq> handler = (PersistConnPbHandler<PlayerActor, GmCommandReq>) ChannelDataMapping.getHandler(IProtocolId.System.GM_COMMAND_REQ);
		GmCommandReq gmCommandReq = GmCommandReq.valueOf(context.getRequestData().getType(), context.getRequestData().getParams());

		playerActor.addMessage(p -> {
			try {
				handler.handler(playerActor, new IPersistConnRequest<GmCommandReq>() {
					@Override
					public Channel channel() {
						return context.channel();
					}

					@Override
					public Object getAttribute(String key) {
						return context.getAttribute(key);
					}

					@Override
					public void setAttribute(String key, Object val) {
						context.setAttribute(key, val);
					}

					@Override
					public GmCommandReq getRequestData() {
						return gmCommandReq;
					}

					@Override
					public String getRemoteAddress() {
						return context.getRemoteAddress();
					}
				});
				actor.sendMessage(new GmDToolsCommandRsp(true, null));
			} catch (Exception e) {
				LoggerType.DUODUO_FLASH_HANDLER.error("GmDToolsCommandHandler", e);
				actor.sendMessage(new GmDToolsCommandRsp(false, e.getMessage()));
			}
		});
	}
}
