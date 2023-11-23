package org.qiunet.cross.node;

import com.google.common.collect.Maps;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.util.Attribute;
import org.qiunet.flash.handler.common.IMessage;
import org.qiunet.flash.handler.common.enums.ServerConnType;
import org.qiunet.flash.handler.common.message.MessageContent;
import org.qiunet.flash.handler.common.player.IMessageActor;
import org.qiunet.flash.handler.context.header.INodeServerHeader;
import org.qiunet.flash.handler.context.request.IRequestContext;
import org.qiunet.flash.handler.context.session.ISession;
import org.qiunet.flash.handler.context.session.NodeServerSession;
import org.qiunet.flash.handler.context.session.NodeSessionType;
import org.qiunet.flash.handler.handler.IHandler;
import org.qiunet.flash.handler.netty.server.constants.CloseCause;
import org.qiunet.flash.handler.netty.server.constants.ServerConstants;
import org.qiunet.flash.handler.netty.server.node.handler.BaseNodeServerHandler;
import org.qiunet.utils.logger.LoggerType;

import java.util.Map;


/**
 * 专给node服务提供的handler
 *
 * Created by qiunet.
 * 23/3/24
 */
public class ServerNodeServerHandler extends BaseNodeServerHandler {

	private final Map<Integer, ISession> sessions = Maps.newHashMap();

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		logger.error("Node server handler exception:", cause);
	}

	@Override
	public void channelRead1(ChannelHandlerContext ctx, MessageContent content, IHandler handler) throws Exception {
		if (!INodeServerHeader.class.isAssignableFrom(content.getHeader().getClass())
				|| ! ((INodeServerHeader) content.getHeader()).isServerNodeMsg()
		) {
			ctx.channel().pipeline().remove(this);
			ctx.fireChannelRead(content.retain());
			return;
		}

		INodeServerHeader header = (INodeServerHeader) content.getHeader();
		Attribute<IMessageActor> actorAttribute = ctx.channel().attr(ServerConstants.MESSAGE_ACTOR_KEY);
		ServerNode serverNode = (ServerNode) actorAttribute.get();
		if (serverNode == null) {
			serverNode = newServerNode(ctx.channel(), (int) header.id());
			actorAttribute.set(serverNode);
		}

		IRequestContext context = handler.getDataType().createRequestContext(serverNode.getSession(), content);
		serverNode.addMessage((IMessage) context);
	}

	private ServerNode newServerNode(Channel channel, int serverId) {
		NodeServerSession session = new NodeServerSession(NodeSessionType.SERVER_NODE, channel, ServerNodeManager.getCurrServerId());
		session.addCloseListener("CloseSession", ((session1, cause) -> {
			NodeServerSession serverNodeSession = (NodeServerSession) session1;
			if (! serverNodeSession.isNoticedRemote()) {
				((ServerNode) session1.getAttachObj(ServerConstants.MESSAGE_ACTOR_KEY)).fireCrossEvent(ServerNodeQuitEvent.valueOf());
				serverNodeSession.setNoticedRemote();
			}
			LoggerType.DUODUO_FLASH_HANDLER.info("====Server Node Server ServerId {} was removed!", serverId);
			sessions.remove(serverId);
		}));

		ServerNode serverNode = new ServerNode(session, serverId);
		session.attachObj(ServerConstants.HANDLER_TYPE_KEY, ServerConnType.TCP);
		session.attachObj(ServerConstants.MESSAGE_ACTOR_KEY, serverNode);
		sessions.put(serverId, session);
		return serverNode;
	}


	@Override
	public void channelInactive(ChannelHandlerContext ctx) throws Exception {
		for (ISession s : sessions.values()) {
			s.close(CloseCause.INACTIVE);
		}
		super.channelInactive(ctx);
	}
}
