package org.qiunet.cross.node;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.util.Attribute;
import org.qiunet.flash.handler.common.IMessage;
import org.qiunet.flash.handler.common.enums.ServerConnType;
import org.qiunet.flash.handler.common.message.MessageContent;
import org.qiunet.flash.handler.common.player.IMessageActor;
import org.qiunet.flash.handler.context.header.IProtocolHeader;
import org.qiunet.flash.handler.context.request.IRequestContext;
import org.qiunet.flash.handler.context.session.NodeSessionType;
import org.qiunet.flash.handler.context.session.ServerNodeSession;
import org.qiunet.flash.handler.handler.IHandler;
import org.qiunet.flash.handler.netty.server.constants.ServerConstants;
import org.qiunet.flash.handler.netty.server.node.handler.BaseNodeServerHandler;
import org.qiunet.utils.logger.LoggerType;


/**
 * 专给node服务提供的handler
 *
 * Created by qiunet.
 * 23/3/24
 */
public class ServerNodeServerHandler extends BaseNodeServerHandler {

	@Override
	public void channelRead1(ChannelHandlerContext ctx, MessageContent content, IHandler handler) throws Exception {
		if (!IProtocolHeader.INodeServerHeader.class.isAssignableFrom(content.getHeader().getClass())
				|| ! ((IProtocolHeader.INodeServerHeader) content.getHeader()).isServerNodeMsg()
		) {
			ctx.fireChannelRead(content.retain());
			return;
		}

		IProtocolHeader.INodeServerHeader header = (IProtocolHeader.INodeServerHeader) content.getHeader();
		Attribute<IMessageActor> actorAttribute = ctx.channel().attr(ServerConstants.MESSAGE_ACTOR_KEY);
		ServerNode serverNode = (ServerNode) actorAttribute.get();
		if (serverNode == null) {
			serverNode = newServerNode(ctx.channel(), (int) header.id());
			actorAttribute.set(serverNode);
		}

		IRequestContext context = handler.getDataType().createRequestContext(serverNode.getSession(), content, ctx.channel());
		serverNode.addMessage((IMessage) context);
	}

	private ServerNode newServerNode(Channel channel, int serverId) {
		ServerNodeSession session = new ServerNodeSession(NodeSessionType.SERVER_NODE, channel, ServerNodeManager.getCurrServerId());
		session.addCloseListener("CloseSession", ((session1, cause) -> {
			ServerNodeSession serverNodeSession = (ServerNodeSession) session1;
			if (! serverNodeSession.isNoticedRemote()) {
				((ServerNode) session1.getAttachObj(ServerConstants.MESSAGE_ACTOR_KEY)).fireCrossEvent(ServerNodeQuitEvent.valueOf());
				serverNodeSession.setNoticedRemote();
			}
			LoggerType.DUODUO_FLASH_HANDLER.info("====Server Node Server ServerId {} was removed!", serverId);
		}));
		ServerNode serverNode = new ServerNode(session, serverId);
		session.attachObj(ServerConstants.HANDLER_TYPE_KEY, ServerConnType.TCP);
		ServerNodeManager0.instance.nodes.putIfAbsent(serverId, serverNode);
		return serverNode;
	}
}
