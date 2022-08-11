package org.qiunet.cross.common.trigger;

import org.qiunet.flash.handler.common.IMessage;
import org.qiunet.flash.handler.common.annotation.SkipDebugOut;
import org.qiunet.flash.handler.common.enums.ServerConnType;
import org.qiunet.flash.handler.common.id.IProtocolId;
import org.qiunet.flash.handler.common.message.MessageContent;
import org.qiunet.flash.handler.common.player.IMessageActor;
import org.qiunet.flash.handler.common.player.PlayerActor;
import org.qiunet.flash.handler.common.protobuf.ProtobufDataManager;
import org.qiunet.flash.handler.context.header.CrossProtocolHeader;
import org.qiunet.flash.handler.context.request.data.ChannelDataMapping;
import org.qiunet.flash.handler.context.request.data.IChannelData;
import org.qiunet.flash.handler.context.request.data.InterestedChannelData;
import org.qiunet.flash.handler.context.request.data.ServerCommunicationData;
import org.qiunet.flash.handler.context.response.push.DefaultByteBufMessage;
import org.qiunet.flash.handler.context.response.push.DefaultBytesMessage;
import org.qiunet.flash.handler.context.session.ISession;
import org.qiunet.flash.handler.handler.IHandler;
import org.qiunet.flash.handler.netty.client.trigger.IPersistConnResponseTrigger;
import org.qiunet.flash.handler.netty.server.constants.ServerConstants;
import org.qiunet.utils.data.ByteUtil;
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
		if (data.getProtocolId() == IProtocolId.System.SERVER_PONG
		|| data.getProtocolId() == IProtocolId.System.CONNECTION_RSP
		) {
			// pong 信息不需要处理
			return;
		}

		PlayerActor playerActor = (PlayerActor) session.getAttachObj(ServerConstants.MESSAGE_ACTOR_KEY);
		if (data.getProtocolId() < 1000
		&& ChannelDataMapping.protocolClass(data.getProtocolId()).isAnnotationPresent(ServerCommunicationData.class)) {
			IHandler handler = ChannelDataMapping.getHandler(data.getProtocolId());
			if (handler == null) {
				LoggerType.DUODUO_CROSS.error("Server not handler protocolId [{}]", data.getProtocolId());
				return;
			}
			IMessage message = handler.getHandlerType().createRequestContext(data, session.channel(), handler, playerActor);
			playerActor.addMessage(message);
			return;
		}

		if (playerActor.waitReconnect()) {
			// 不感兴趣的不管
			if (! ChannelDataMapping.protocolClass(data.getProtocolId()).isAnnotationPresent(InterestedChannelData.class)) {
				return;
			}

			synchronized (playerActor) {
				DefaultBytesMessage message = new DefaultBytesMessage(data.getProtocolId(), ByteUtil.readBytebuffer(data.byteBuffer()));
				playerActor.getVal(ServerConstants.INTEREST_MESSAGE_LIST).add(message);
			}
			return;
		}

		DefaultByteBufMessage message = DefaultByteBufMessage.valueOf(data.getProtocolId(), data.byteBuf());
		CrossProtocolHeader header = (CrossProtocolHeader) data.getHeader();
		boolean flush = header.isFlush();
		boolean kcp = header.isKcp();
		playerActor.addMessage(m -> {
			try {
				c2pMessage(playerActor, message, kcp, flush);
			}catch (Exception e) {
				if (message.getContent() != null && message.getContent().refCnt() > 0) {
					message.getContent().release();
				}
			}
		});
		data.recycle();
	}

	private void c2pMessage(IMessageActor iMessageActor, DefaultByteBufMessage message, boolean kcp, boolean flush) {
		if (logger.isInfoEnabled()) {
			Class<? extends IChannelData> aClass = ChannelDataMapping.protocolClass(message.getProtocolID());
			if (! aClass.isAnnotationPresent(SkipDebugOut.class)) {
				IChannelData channelData = ProtobufDataManager.decode(aClass, message.byteBuffer());
				ServerConnType serverConnType = iMessageActor.getSender().channel().attr(ServerConstants.HANDLER_TYPE_KEY).get();
				logger.info("{} C2P {} message: {}", iMessageActor.getIdentity(), kcp  ? "KCP": serverConnType, ToString.toString(channelData));
			}
		}

		if (kcp && iMessageActor.isKcpSessionPrepare()) {
			iMessageActor.getSender().sendKcpMessage(message);
		}else {
			iMessageActor.getSender().sendMessage(message, kcp || flush);
		}
	}
}
