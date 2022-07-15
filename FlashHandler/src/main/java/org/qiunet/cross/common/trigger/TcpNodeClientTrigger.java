package org.qiunet.cross.common.trigger;

import org.qiunet.flash.handler.common.IMessage;
import org.qiunet.flash.handler.common.id.IProtocolId;
import org.qiunet.flash.handler.common.message.MessageContent;
import org.qiunet.flash.handler.common.player.IMessageActor;
import org.qiunet.flash.handler.context.request.data.ChannelDataMapping;
import org.qiunet.flash.handler.context.session.ISession;
import org.qiunet.flash.handler.handler.IHandler;
import org.qiunet.flash.handler.netty.client.trigger.IPersistConnResponseTrigger;
import org.qiunet.flash.handler.netty.server.constants.ServerConstants;
import org.qiunet.utils.logger.LoggerType;

/***
 * Cross Tcp客户端响应处理
 *
 * @author qiunet
 * 2020-10-23 17:44
 */
public class TcpNodeClientTrigger implements IPersistConnResponseTrigger {
	@Override
	public void response(ISession session, MessageContent data) {
		if (data.getProtocolId() == IProtocolId.System.SERVER_PONG
		|| data.getProtocolId() == IProtocolId.System.CONNECTION_RSP
		|| data.getProtocolId() == IProtocolId.System.SERVER_EXCEPTION
		) {
			// pong 信息不需要处理
			return;
		}


		IMessageActor iMessageActor = session.getAttachObj(ServerConstants.MESSAGE_ACTOR_KEY);
		IHandler handler = ChannelDataMapping.getHandler(data.getProtocolId());
		if (handler == null) {
			LoggerType.DUODUO_CROSS.error("Server not handler protocolId [{}]", data.getProtocolId());
		}
		IMessage message = handler.getHandlerType().createRequestContext(data, session.channel(), handler, iMessageActor);
		iMessageActor.addMessage(message);
	}
}
