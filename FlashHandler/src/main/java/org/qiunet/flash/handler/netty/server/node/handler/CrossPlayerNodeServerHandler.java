package org.qiunet.flash.handler.netty.server.node.handler;

import com.google.common.collect.Maps;
import io.netty.channel.ChannelHandlerContext;
import org.qiunet.cross.actor.CrossPlayerActor;
import org.qiunet.flash.handler.common.IMessage;
import org.qiunet.flash.handler.common.enums.ServerConnType;
import org.qiunet.flash.handler.common.id.IProtocolId;
import org.qiunet.flash.handler.common.message.MessageContent;
import org.qiunet.flash.handler.common.player.UserOnlineManager;
import org.qiunet.flash.handler.context.header.INodeServerHeader;
import org.qiunet.flash.handler.context.request.IRequestContext;
import org.qiunet.flash.handler.context.request.persistconn.PersistConnPbRequestContext;
import org.qiunet.flash.handler.context.session.ISession;
import org.qiunet.flash.handler.context.session.NodeServerSession;
import org.qiunet.flash.handler.context.session.NodeSessionType;
import org.qiunet.flash.handler.handler.IHandler;
import org.qiunet.flash.handler.netty.server.constants.CloseCause;
import org.qiunet.flash.handler.netty.server.constants.ServerConstants;

import java.util.Map;


/**
 * 专给node服务提供的handler
 *
 * Created by qiunet.
 * 23/3/24
 */
public class CrossPlayerNodeServerHandler extends BaseNodeServerHandler {

	private final Map<Long, ISession> sessions = Maps.newHashMap();

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		logger.error("Node server handler exception:", cause);
	}

	@Override
	public void channelRead1(ChannelHandlerContext ctx, MessageContent content, IHandler handler) throws Exception {
		if (!INodeServerHeader.class.isAssignableFrom(content.getHeader().getClass())
		|| ! ((INodeServerHeader) content.getHeader()).isPlayerMsg()
		) {
			ctx.channel().pipeline().remove(this);
			ctx.fireChannelRead(content.retain());
			return;
		}

		INodeServerHeader header = (INodeServerHeader) content.getHeader();
		CrossPlayerActor crossPlayerActor = UserOnlineManager.instance.getCrossPlayerActor(header.id());
		if (crossPlayerActor == null) {
			if (content.getProtocolId() != IProtocolId.System.CROSS_PLAYER_AUTH) {
				logger.error("Cross Player id {} protocolId: {} not authorize access!", header.id(), content.getProtocolId());
				return;
			}
			crossPlayerActor = this.newCrossPlayerActor(ctx, header.id());
		}
		IRequestContext context = handler.getDataType().createRequestContext(crossPlayerActor.getSession(), content);
		if (content.getProtocolId() == IProtocolId.System.CROSS_PLAYER_AUTH) {
			((PersistConnPbRequestContext) context).execute(crossPlayerActor);
			return;
		}
		crossPlayerActor.addMessage((IMessage) context);
	}

	/**
	 * 连接用户
	 * @param ctx
 	 * @param playerId
	 */
	private CrossPlayerActor newCrossPlayerActor(ChannelHandlerContext ctx, long playerId) {
		NodeServerSession session = new NodeServerSession(NodeSessionType.CROSS_PLAYER, ctx.channel(), playerId);
		CrossPlayerActor crossPlayerActor = new CrossPlayerActor(session, String.valueOf(playerId));
		session.attachObj(ServerConstants.HANDLER_TYPE_KEY, ServerConnType.TCP);
		session.attachObj(ServerConstants.MESSAGE_ACTOR_KEY, crossPlayerActor);
		session.addCloseListener("NodeServerHandlerRemoveSession", (s, c) -> {
			sessions.remove(playerId);
		});
		sessions.put(playerId, session);
		return crossPlayerActor;
	}

	@Override
	public void channelInactive(ChannelHandlerContext ctx) throws Exception {
		for (ISession s : sessions.values()) {
			s.close(CloseCause.INACTIVE);
		}
		super.channelInactive(ctx);
	}
}
