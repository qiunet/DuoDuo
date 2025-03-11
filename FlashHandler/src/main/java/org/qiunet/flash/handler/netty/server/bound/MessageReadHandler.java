package org.qiunet.flash.handler.netty.server.bound;

import com.google.common.base.Preconditions;
import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.qiunet.data.conf.ServerConfig;
import org.qiunet.flash.handler.common.IMessage;
import org.qiunet.flash.handler.common.annotation.SkipDebugOut;
import org.qiunet.flash.handler.common.id.IProtocolId;
import org.qiunet.flash.handler.common.message.MessageContent;
import org.qiunet.flash.handler.common.player.AbstractMessageActor;
import org.qiunet.flash.handler.common.player.ICrossStatusActor;
import org.qiunet.flash.handler.common.player.IMessageActor;
import org.qiunet.flash.handler.common.player.PlayerActor;
import org.qiunet.flash.handler.common.protobuf.ProtobufDataManager;
import org.qiunet.flash.handler.context.header.IProtocolHeader;
import org.qiunet.flash.handler.context.request.IRequestContext;
import org.qiunet.flash.handler.context.request.data.ChannelDataMapping;
import org.qiunet.flash.handler.context.request.data.IChannelData;
import org.qiunet.flash.handler.context.response.push.DefaultByteBufMessage;
import org.qiunet.flash.handler.context.session.ISession;
import org.qiunet.flash.handler.context.status.IGameStatus;
import org.qiunet.flash.handler.context.status.StatusResultException;
import org.qiunet.flash.handler.handler.IHandler;
import org.qiunet.flash.handler.handler.persistconn.IPersistConnHandler;
import org.qiunet.flash.handler.netty.server.config.ServerBootStrapConfig;
import org.qiunet.flash.handler.netty.server.config.adapter.message.ClientPingRequest;
import org.qiunet.flash.handler.netty.server.config.adapter.message.HandlerNotFoundResponse;
import org.qiunet.flash.handler.netty.server.config.adapter.message.ServerPongResponse;
import org.qiunet.flash.handler.netty.server.constants.CloseCause;
import org.qiunet.flash.handler.netty.server.constants.ServerConstants;
import org.qiunet.flash.handler.netty.server.message.ConnectionReq;
import org.qiunet.flash.handler.netty.server.message.ConnectionRsp;
import org.qiunet.flash.handler.netty.transmit.ITransmitHandler;
import org.qiunet.flash.handler.util.ChannelUtil;
import org.qiunet.function.gm.proto.req.GmDebugProtocolReq;
import org.qiunet.function.gm.proto.rsp.GmDebugProtocolRsp;
import org.qiunet.utils.json.JsonUtil;
import org.qiunet.utils.logger.LoggerType;
import org.qiunet.utils.string.StringUtil;
import org.slf4j.Logger;

import java.nio.ByteBuffer;

/***
 * 读取消息的handler
 *
 * @author qiunet
 * 2023/4/20 12:20
 */
public class MessageReadHandler extends SimpleChannelInboundHandler<MessageContent> {
	private static final Logger logger = LoggerType.DUODUO_FLASH_HANDLER.getLogger();
	private static final IChannelData HANDLER_NOT_FOUND = new HandlerNotFoundResponse();

	/**
	 * 处理ping信息
	 * @param channel
	 * @param content
	 * @return
	 */
	private boolean handlerPing(Channel channel, MessageContent content) {
		if (content.getProtocolId() != IProtocolId.System.CLIENT_PING) {
			return false;
		}

		ClientPingRequest pingRequest = content.decodeProtobuf(ClientPingRequest.class);
		ServerPongResponse serverPongResponse = ServerPongResponse.valueOf(pingRequest.getBytes());
		channel.writeAndFlush(serverPongResponse.buildChannelMessage());
		return true;
	}

	@Override
	protected void channelRead0(ChannelHandlerContext ctx, MessageContent content) throws Exception {
		Channel channel = ctx.channel();
		if (this.handlerPing(channel, content)) {
			return;
		}

		ServerBootStrapConfig config = channel.attr(ServerConstants.BOOTSTRAP_CONFIG_KEY).get();
		ISession session = ChannelUtil.getSession(channel);
		Preconditions.checkNotNull(session);

		if (content.getProtocolId() == IProtocolId.System.CONNECTION_REQ) {
			ConnectionReq connectionReq = ProtobufDataManager.decode(ConnectionReq.class, content.byteBuffer());
			if (logger.isInfoEnabled()) {
				logger.info("[{}] <<< {}", session, connectionReq._toString());
			}

			if (StringUtil.isEmpty(connectionReq.getIdKey())) {
				session.close(CloseCause.CONNECTION_ID_KEY_ERROR);
				return;
			}

			if (! config.getStartupContext().userConnectionCheck(connectionReq.getIdKey())) {
				session.close(CloseCause.FORBID_ACCOUNT);
				return;
			}

			PlayerActor playerActor = new PlayerActor(session, connectionReq.getIdKey());
			session.attachObj(ServerConstants.MESSAGE_ACTOR_KEY, playerActor);
			playerActor.sendMessage(ConnectionRsp.getInstance(), true);
			return;
		}

		AbstractMessageActor messageActor = (AbstractMessageActor) session.getAttachObj(ServerConstants.MESSAGE_ACTOR_KEY);
		if (messageActor == null) {
			logger.info("{} messageActor is null! Need call ConnectionReq first", channel.id().asShortText());
			session.close(CloseCause.CONNECTION_ID_KEY_ERROR);
			return;
		}

		if (! config.getStartupContext().userServerValidate(session)) {
			return;
		}

		IHandler handler = ChannelDataMapping.getHandler(content.getProtocolId());
		if (handler == null) {
			channel.writeAndFlush(HANDLER_NOT_FOUND);
			return;
		}

		if (content.getProtocolId() == IProtocolId.System.GM_DEBUG_PROTOCOL_REQ) {
			// 执行的gm命令
			this.gmDebugProtocol(session, content);
			return;
		}

		this.processHandler(session, handler, content);
	}

	private void gmDebugProtocol(ISession session, MessageContent content) {
		if (ServerConfig.isOfficial()) {
			LoggerType.DUODUO.error("Current is official server. can not access gm command!");
			return;
		}

		GmDebugProtocolReq protocolReq = content.decodeProtobuf(GmDebugProtocolReq.class);
		int protocolID = protocolReq.getProtocolId();
		String data = protocolReq.getData();
		Class<? extends IChannelData> aClass = ChannelDataMapping.protocolClass(protocolID);
		if (aClass == null) {
			throw StatusResultException.valueOf(IGameStatus.FAIL);
		}

		IHandler<IChannelData> handler = ChannelDataMapping.getHandler(protocolID);
		if (handler == null || ! IPersistConnHandler.class.isAssignableFrom(handler.getClass())) {
			throw StatusResultException.valueOf(IGameStatus.FAIL);
		}

		IChannelData channelData = JsonUtil.getGeneralObj(data, aClass);
		ByteBuf byteBuf = channelData.toByteBuf();
		MessageContent messageContent = MessageContent.valueOf(new IProtocolHeader.ProtocolHeader() {
			@Override
			public void recycle() {
			}

			@Override
			public int getProtocolId() {
				return protocolID;
			}

			@Override
			public ByteBuf headerByteBuf() {
				// 不会调用到
				return null;
			}

			@Override
			public boolean isValidMessage() {
				return true;
			}

			@Override
			public int getLength() {
				return byteBuf.readableBytes();
			}

			@Override
			public boolean validEncryption(ByteBuffer buffer) {
				return true;
			}
		}, byteBuf);
		try {
			this.processHandler(session, handler, messageContent);
		}finally {
			messageContent.release();
		}

		// 上面throw exception 不会执行下面.
		session.sendMessage(GmDebugProtocolRsp.valueOf());
	}
	/**
	 * 正式处理handler
	 * @param session
	 * @param handler
	 * @param content
	 */
	private void processHandler(ISession session, IHandler handler, MessageContent content) {
		IMessageActor messageActor = session.getAttachObj(ServerConstants.MESSAGE_ACTOR_KEY);

		if (handler instanceof ITransmitHandler && messageActor instanceof ICrossStatusActor && ((ICrossStatusActor) messageActor).isCrossStatus()) {
			DefaultByteBufMessage message = DefaultByteBufMessage.valueOf(content.getProtocolId(), content.byteBuf());
			// 回收content. 并防止里面的byteBuf被回收.
			content.recycle();
			messageActor.runMessageWithMsgExecuteIndex(m -> {
				try {
					ISession crossSession = ((ICrossStatusActor) m).currentCrossSession();
					if (logger.isInfoEnabled()) {
						Class<? extends IChannelData> aClass = ChannelDataMapping.protocolClass(message.getProtocolID());
						if (SkipDebugOut.DebugOut.test(aClass)) {
							IChannelData channelData = ProtobufDataManager.decode(aClass, message.byteBuffer());
							logger.info("[{}] transmit {} data: {}", crossSession, session.getAttachObj(ServerConstants.HANDLER_TYPE_KEY), channelData._toString());
						}
					}
					((ICrossStatusActor) messageActor).sendCrossMessage(message);
				} catch (Exception e) {
					if (message.getContent() != null && message.getContent().refCnt() > 0) {
						message.getContent().release();
					}
				}
			}, String.valueOf(messageActor.msgExecuteIndex()));
			return;
		}
		if (session.isActive()) {
			IRequestContext context = handler.getDataType().createRequestContext(session, content);
			messageActor.addMessage((IMessage) context);
		}
	}
}
