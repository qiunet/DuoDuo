package org.qiunet.flash.handler.netty.server.node.handler;

import io.netty.channel.ChannelHandlerContext;
import org.qiunet.cross.actor.CrossPlayerActor;
import org.qiunet.cross.node.ServerNodeManager;
import org.qiunet.flash.handler.common.IMessage;
import org.qiunet.flash.handler.common.enums.ServerConnType;
import org.qiunet.flash.handler.common.message.MessageContent;
import org.qiunet.flash.handler.common.player.UserOnlineManager;
import org.qiunet.flash.handler.common.player.proto.CrossPlayerLogoutPush;
import org.qiunet.flash.handler.context.header.IProtocolHeader;
import org.qiunet.flash.handler.context.request.IRequestContext;
import org.qiunet.flash.handler.context.session.NodeServerSession;
import org.qiunet.flash.handler.context.session.NodeSessionType;
import org.qiunet.flash.handler.handler.IHandler;
import org.qiunet.flash.handler.netty.server.constants.ServerConstants;


/**
 * 专给node服务提供的handler
 *
 * Created by qiunet.
 * 23/3/24
 */
public class PlayerNodeServerHandler extends BaseNodeServerHandler {

	@Override
	public void channelRead1(ChannelHandlerContext ctx, MessageContent content, IHandler handler) throws Exception {
		if (!IProtocolHeader.INodeServerHeader.class.isAssignableFrom(content.getHeader().getClass())
		|| ! ((IProtocolHeader.INodeServerHeader) content.getHeader()).isPlayerMsg()
		) {
			ctx.fireChannelRead(content.retain());
			return;
		}

		if (ServerNodeManager.isDeprecatedServer()) {
			// 发送过期服务器推送 让客户端退出
			ctx.writeAndFlush(CrossPlayerLogoutPush.instance.buildChannelMessage());
		}

		IProtocolHeader.INodeServerHeader header = (IProtocolHeader.INodeServerHeader) content.getHeader();
		CrossPlayerActor crossPlayerActor = UserOnlineManager.instance.getOrCreateCrossPlayerActor(header.id(), id -> this.newCrossPlayerActor(ctx, id, header.getServerId()));
		// 及时更新channel 避免channel可能失效的可能.
		((NodeServerSession) crossPlayerActor.getSession()).setChannel(ctx.channel());
		IRequestContext context = handler.getDataType().createRequestContext(crossPlayerActor.getSession(), content, ctx.channel());
		crossPlayerActor.addMessage((IMessage) context);
	}

	/**
	 * 连接用户
	 * @param ctx
 	 * @param playerId
	 */
	private CrossPlayerActor newCrossPlayerActor(ChannelHandlerContext ctx, long playerId, int serverId) {
		NodeServerSession session = new NodeServerSession(NodeSessionType.CROSS_PLAYER, ctx.channel(), playerId);
		session.attachObj(ServerConstants.HANDLER_TYPE_KEY, ServerConnType.TCP);
		CrossPlayerActor crossPlayerActor = new CrossPlayerActor(session);
		crossPlayerActor.setMsgExecuteIndex(String.valueOf(playerId));
		crossPlayerActor.setServerId(serverId);
		crossPlayerActor.auth(playerId);
		return crossPlayerActor;
	}
}
