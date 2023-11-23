package org.qiunet.cross.pool;

import io.netty.channel.Channel;
import org.qiunet.flash.handler.common.IMessage;
import org.qiunet.flash.handler.common.id.IProtocolId;
import org.qiunet.flash.handler.common.message.MessageContent;
import org.qiunet.flash.handler.common.player.IMessageActor;
import org.qiunet.flash.handler.common.player.PlayerActor;
import org.qiunet.flash.handler.context.header.INodeServerHeader;
import org.qiunet.flash.handler.context.request.IRequestContext;
import org.qiunet.flash.handler.context.request.data.ChannelDataMapping;
import org.qiunet.flash.handler.context.request.data.ServerCommunicationData;
import org.qiunet.flash.handler.context.session.ISession;
import org.qiunet.flash.handler.handler.IHandler;
import org.qiunet.flash.handler.netty.server.constants.ServerConstants;
import org.qiunet.utils.logger.LoggerType;

/***
 * 节点通讯响应触发
 * @author qiunet
 * 2023/3/28 14:27
 */
public interface NodeChannelTrigger {

	default boolean serverNode() {
		return false;
	}

	default void response(ISession session, Channel channel, MessageContent data) {
		if (data.getProtocolId() == IProtocolId.System.SERVER_PONG
			|| data.getProtocolId() == IProtocolId.System.CLIENT_PING
			|| data.getProtocolId() == IProtocolId.System.CONNECTION_RSP
			|| data.getProtocolId() == IProtocolId.System.SERVER_EXCEPTION
		) {
			return;
		}

		if (serverNode() || (data.getProtocolId() < 1000
				&& ChannelDataMapping.protocolClass(data.getProtocolId()).isAnnotationPresent(ServerCommunicationData.class))) {
			IHandler handler = ChannelDataMapping.getHandler(data.getProtocolId());
			if (handler == null) {
				LoggerType.DUODUO_CROSS.error("Server not handler protocolId [{}]", data.getProtocolId());
				return;
			}

			IMessageActor iMessageActor = session.getAttachObj(ServerConstants.MESSAGE_ACTOR_KEY);
			IRequestContext message = handler.getHandlerType().createRequestContext(handler, session, data);
			iMessageActor.addMessage((IMessage<PlayerActor>) message);
			return;
		}

		this.response0(session, channel, data);
	}

	/**
	 *  如果有. 下一步处理
	 * @param session session
	 * @param channel
	 * @param data
	 */
	default void response0(ISession session, Channel channel, MessageContent data){}
	/**
	 * 根据头信息的id 获取session
	 * @param header 头信息
	 * @return session
	 */
	ISession getNodeSession(Channel channel, INodeServerHeader header);
}
