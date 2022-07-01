package org.qiunet.cross.common.trigger;

import org.qiunet.flash.handler.common.annotation.SkipDebugOut;
import org.qiunet.flash.handler.common.enums.ServerConnType;
import org.qiunet.flash.handler.common.id.IProtocolId;
import org.qiunet.flash.handler.common.message.MessageContent;
import org.qiunet.flash.handler.common.player.IMessageActor;
import org.qiunet.flash.handler.common.protobuf.ProtobufDataManager;
import org.qiunet.flash.handler.context.header.CrossProtocolHeader;
import org.qiunet.flash.handler.context.request.data.ChannelDataMapping;
import org.qiunet.flash.handler.context.request.data.IChannelData;
import org.qiunet.flash.handler.context.request.data.ServerCommunicationData;
import org.qiunet.flash.handler.context.response.push.DefaultByteBufMessage;
import org.qiunet.flash.handler.context.session.ISession;
import org.qiunet.flash.handler.handler.IHandler;
import org.qiunet.flash.handler.netty.client.trigger.IPersistConnResponseTrigger;
import org.qiunet.flash.handler.netty.server.constants.ServerConstants;
import org.qiunet.utils.logger.LoggerType;
import org.qiunet.utils.string.ToString;
import org.slf4j.Logger;

/***
 * player Tcp客户端响应处理
 *
 * @author qiunet
 * 2020-10-23 17:44
 */
public class PlayerConnectorClientTrigger implements IPersistConnResponseTrigger {

	private static final Logger logger = LoggerType.DUODUO_FLASH_HANDLER.getLogger();


	@Override
	public void response(ISession session, MessageContent data) {
		if (data.getProtocolId() == IProtocolId.System.SERVER_PONG) {
			// pong 信息不需要处理
			return;
		}

		IMessageActor iMessageActor = session.getAttachObj(ServerConstants.MESSAGE_ACTOR_KEY);
		if (data.getProtocolId() < 1000
		&& ChannelDataMapping.protocolClass(data.getProtocolId()).isAnnotationPresent(ServerCommunicationData.class)) {
			IHandler handler = ChannelDataMapping.getHandler(data.getProtocolId());
			if (handler == null) {
				LoggerType.DUODUO_CROSS.error("Server not handler protocolId [{}]", data.getProtocolId());
				return;
			}

			iMessageActor.addMessage(handler.getHandlerType().createRequestContext(data.retain(), session.channel(), handler, iMessageActor));
			return;
		}

		data.retain();
		iMessageActor.addMessage(m -> {
			c2pMessage(iMessageActor, data);
		});
	}

	private void c2pMessage(IMessageActor iMessageActor, MessageContent data) {
		CrossProtocolHeader header = (CrossProtocolHeader) data.getHeader();
		if (logger.isInfoEnabled()) {
			Class<? extends IChannelData> aClass = ChannelDataMapping.protocolClass(data.getProtocolId());
			if (! aClass.isAnnotationPresent(SkipDebugOut.class)) {
				IChannelData channelData = ProtobufDataManager.decode(aClass, data.byteBuffer());
				ServerConnType serverConnType = iMessageActor.getSender().channel().attr(ServerConstants.HANDLER_TYPE_KEY).get();
				logger.info("{} C2P {} message: {}", iMessageActor.getIdentity(), header.isKcp()  ? "KCP": serverConnType, ToString.toString(channelData));
			}
		}

		DefaultByteBufMessage message = new DefaultByteBufMessage(data.getProtocolId(), data.byteBuf());
		if (header.isKcp() && iMessageActor.isKcpSessionPrepare()) {
			iMessageActor.getSender().sendKcpMessage(message);
		}else {
			iMessageActor.getSender().sendMessage(message, header.isKcp() || header.isFlush());
		}
	}
}
