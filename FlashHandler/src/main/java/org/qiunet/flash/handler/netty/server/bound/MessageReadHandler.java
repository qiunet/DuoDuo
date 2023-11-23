package org.qiunet.flash.handler.netty.server.bound;

import com.google.common.base.Preconditions;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.qiunet.flash.handler.common.id.IProtocolId;
import org.qiunet.flash.handler.common.message.MessageContent;
import org.qiunet.flash.handler.common.player.AbstractMessageActor;
import org.qiunet.flash.handler.common.player.PlayerActor;
import org.qiunet.flash.handler.common.protobuf.ProtobufDataManager;
import org.qiunet.flash.handler.context.request.data.ChannelDataMapping;
import org.qiunet.flash.handler.context.request.data.IChannelData;
import org.qiunet.flash.handler.context.session.ISession;
import org.qiunet.flash.handler.handler.IHandler;
import org.qiunet.flash.handler.netty.server.config.ServerBootStrapConfig;
import org.qiunet.flash.handler.netty.server.config.adapter.message.HandlerNotFoundResponse;
import org.qiunet.flash.handler.netty.server.constants.CloseCause;
import org.qiunet.flash.handler.netty.server.constants.ServerConstants;
import org.qiunet.flash.handler.netty.server.message.ConnectionReq;
import org.qiunet.flash.handler.netty.server.message.ConnectionRsp;
import org.qiunet.flash.handler.util.ChannelUtil;
import org.qiunet.utils.logger.LoggerType;
import org.qiunet.utils.string.StringUtil;
import org.slf4j.Logger;

/***
 * 读取消息的handler
 *
 * @author qiunet
 * 2023/4/20 12:20
 */
public class MessageReadHandler extends SimpleChannelInboundHandler<MessageContent> {
	private static final Logger logger = LoggerType.DUODUO_FLASH_HANDLER.getLogger();
	private static final IChannelData HANDLER_NOT_FOUND = new HandlerNotFoundResponse();

	@Override
	protected void channelRead0(ChannelHandlerContext ctx, MessageContent content) throws Exception {
		Channel channel = ctx.channel();
		if (ChannelUtil.handlerPing(channel, content)) {
			return;
		}

		ServerBootStrapConfig config = channel.attr(ServerConstants.BOOTSTRAP_CONFIG_KEY).get();
		ISession session = ChannelUtil.getSession(channel);
		Preconditions.checkNotNull(session);

		if (! config.getStartupContext().userServerValidate(session)) {
			return;
		}

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
			logger.info("{} msgExecuteIndex is null! Need call ConnectionReq first", messageActor.getIdentity());
			session.close(CloseCause.CONNECTION_ID_KEY_ERROR);
			return;
		}

		IHandler handler = ChannelDataMapping.getHandler(content.getProtocolId());
		if (handler == null) {
			channel.writeAndFlush(HANDLER_NOT_FOUND);
			return;
		}

		ChannelUtil.processHandler(session, handler, content);
	}
}
