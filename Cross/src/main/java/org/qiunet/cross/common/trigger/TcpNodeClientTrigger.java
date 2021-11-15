package org.qiunet.cross.common.trigger;

import org.qiunet.cross.actor.message.Cross2PlayerResponse;
import org.qiunet.flash.handler.common.IMessage;
import org.qiunet.flash.handler.common.id.IProtocolId;
import org.qiunet.flash.handler.common.message.MessageContent;
import org.qiunet.flash.handler.common.player.AbstractPlayerActor;
import org.qiunet.flash.handler.common.player.IMessageActor;
import org.qiunet.flash.handler.common.protobuf.ProtobufDataManager;
import org.qiunet.flash.handler.context.request.data.ChannelDataMapping;
import org.qiunet.flash.handler.context.request.data.IChannelData;
import org.qiunet.flash.handler.context.session.DSession;
import org.qiunet.flash.handler.handler.IHandler;
import org.qiunet.flash.handler.netty.client.trigger.IPersistConnResponseTrigger;
import org.qiunet.flash.handler.netty.server.constants.ServerConstants;

/***
 * Cross Tcp客户端响应处理
 *
 * @author qiunet
 * 2020-10-23 17:44
 */
public class TcpNodeClientTrigger implements IPersistConnResponseTrigger {
	@Override
	public void response(DSession session, MessageContent data) {
		IMessageActor iMessageActor = session.getAttachObj(ServerConstants.MESSAGE_ACTOR_KEY);

		if (data.getProtocolId() == IProtocolId.System.CROSS_2_PLAYER_MSG
			&& iMessageActor instanceof AbstractPlayerActor) {
			// 直接往客户端转发
			Cross2PlayerResponse response = ProtobufDataManager.decode(Cross2PlayerResponse.class, data.bytes());
			Class<? extends IChannelData> protocolClass = ChannelDataMapping.protocolClass(response.getPid());
			IChannelData channelData = ProtobufDataManager.decode(protocolClass, response.getBytes());
			iMessageActor.getSender().sendMessage(channelData);
			return;
		}

		IHandler handler = ChannelDataMapping.getHandler(data.getProtocolId());
		IMessage message = handler.getHandlerType().createRequestContext(data, session.channel(), handler, iMessageActor);
		iMessageActor.addMessage(message);
	}
}
