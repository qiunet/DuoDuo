package org.qiunet.cross.common.trigger;

import org.qiunet.cross.actor.message.Cross2PlayerResponse;
import org.qiunet.flash.handler.common.IMessage;
import org.qiunet.flash.handler.common.id.IProtocolId;
import org.qiunet.flash.handler.common.message.MessageContent;
import org.qiunet.flash.handler.common.player.IMessageActor;
import org.qiunet.flash.handler.common.player.PlayerActor;
import org.qiunet.flash.handler.common.protobuf.ProtobufDataManager;
import org.qiunet.flash.handler.context.request.data.ChannelDataMapping;
import org.qiunet.flash.handler.context.response.push.DefaultBytesMessage;
import org.qiunet.flash.handler.context.response.push.IChannelMessage;
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
		if (data.getProtocolId() == IProtocolId.System.SERVER_PONG) {
			data.release();
			// pong 信息不需要处理
			return;
		}

		IMessageActor iMessageActor = session.getAttachObj(ServerConstants.MESSAGE_ACTOR_KEY);
		if (data.getProtocolId() == IProtocolId.System.CROSS_2_PLAYER_MSG
			&& iMessageActor instanceof PlayerActor) {
			// 直接往客户端转发
			Cross2PlayerResponse response = ProtobufDataManager.decode(Cross2PlayerResponse.class, data.bytes());
			IChannelMessage<byte []> message = new DefaultBytesMessage(response.getPid(), response.getBytes());
			LoggerType.DUODUO_FLASH_HANDLER.debug("tcp node trigger Data.protocolId: {}", response.getPid());
			if (response.isKcpChannel()) {
				iMessageActor.getSender().sendKcpMessage(message);
			}else {
				iMessageActor.getSender().sendMessage(message, response.isFlush());
			}
			return;
		}

		if (data.getProtocolId() == IProtocolId.System.SERVER_EXCEPTION) {
			data.release();
			return;
		}

		IHandler handler = ChannelDataMapping.getHandler(data.getProtocolId());
		if (handler == null) {
			LoggerType.DUODUO_CROSS.error("Server not handler protocolId [{}]", data.getProtocolId());
		}
		IMessage message = handler.getHandlerType().createRequestContext(data, session.channel(), handler, iMessageActor);
		iMessageActor.addMessage(message);
	}
}
