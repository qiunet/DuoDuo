package org.qiunet.flash.handler.util;

import com.google.common.base.Preconditions;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.util.Attribute;
import org.qiunet.flash.handler.common.IMessage;
import org.qiunet.flash.handler.common.annotation.SkipDebugOut;
import org.qiunet.flash.handler.common.event.ClientPingEvent;
import org.qiunet.flash.handler.common.id.IProtocolId;
import org.qiunet.flash.handler.common.message.MessageContent;
import org.qiunet.flash.handler.common.player.ICrossStatusActor;
import org.qiunet.flash.handler.common.player.IMessageActor;
import org.qiunet.flash.handler.common.player.PlayerActor;
import org.qiunet.flash.handler.common.protobuf.ProtobufDataManager;
import org.qiunet.flash.handler.context.header.IProtocolHeader;
import org.qiunet.flash.handler.context.request.IRequestContext;
import org.qiunet.flash.handler.context.request.data.ChannelDataMapping;
import org.qiunet.flash.handler.context.request.data.IChannelData;
import org.qiunet.flash.handler.context.response.push.DefaultByteBufMessage;
import org.qiunet.flash.handler.context.session.HttpSession;
import org.qiunet.flash.handler.context.session.ISession;
import org.qiunet.flash.handler.handler.IHandler;
import org.qiunet.flash.handler.netty.server.config.adapter.message.ClientPingRequest;
import org.qiunet.flash.handler.netty.server.config.adapter.message.ServerPongResponse;
import org.qiunet.flash.handler.netty.server.constants.CloseCause;
import org.qiunet.flash.handler.netty.server.constants.ServerConstants;
import org.qiunet.flash.handler.netty.transmit.ITransmitHandler;
import org.qiunet.utils.logger.LoggerType;
import org.qiunet.utils.string.StringUtil;
import org.slf4j.Logger;

import static io.netty.handler.codec.http.HttpVersion.HTTP_1_1;

public final class ChannelUtil {
	private static final Logger logger = LoggerType.DUODUO_FLASH_HANDLER.getLogger();
	private ChannelUtil(){}
	/**
	 * 关闭通道 打印日志
	 * @param channel 通道
	 */
	public static void closeChannel(Channel channel , CloseCause cause, String errMessage, Object ... params) {
		ISession session = getSession(channel);
		errMessage = StringUtil.slf4jFormat(errMessage, params);
		if (session == null) {
			logger.error(StringUtil.slf4jFormat("Channel [{}] cause [{}] message: {}", channel.id().asShortText(), cause, errMessage));
			channel.close();
			return;
		}

		logger.error(StringUtil.slf4jFormat("session[{}] cause [{}] message:{}", session, cause, errMessage));
		session.close(cause);
	}
	/***
	 * 得到channel保存的ProtocolHeader数据
	 * @param channel
	 * @return
	 */
	public static IProtocolHeader getProtocolHeader(Channel channel) {
		return channel.attr(ServerConstants.PROTOCOL_HEADER).get();
	}
	/***
	 * 关联Session 和 channel
	 * @param session
	 */
	public static void bindSession(ISession session, Channel channel) {
		Preconditions.checkNotNull(session);
		Attribute<ISession> attr = channel.attr(ServerConstants.SESSION_KEY);
		boolean result = attr.compareAndSet(null, session);
		if (! result) {
			logger.error("Session [{}] Duplicate", session);
			session.close(CloseCause.LOGIN_REPEATED);
		}
	}

	/**
	 * 是否是首次连接
	 * @param channel channel
	 * @return true 是. false 不是
	 */
	public static boolean isConnectReq(Channel channel) {
		if (channel == null) {
			return false;
		}

		Attribute<Boolean> attr = channel.attr(ServerConstants.ALREADY_CONNECT_KEY);
		return attr.setIfAbsent(true) == null;
	}

	/***
	 * 得到一个Session
	 * @param channel
	 * @return
	 */
	public static ISession getSession(Channel channel) {
		return channel.attr(ServerConstants.SESSION_KEY).get();
	}


	/**
	 * 处理ping信息
	 * @param channel
	 * @param content
	 * @return
	 */
	public static boolean handlerPing(Channel channel, MessageContent content) {
		if (content.getProtocolId() != IProtocolId.System.CLIENT_PING) {
			return false;
		}
		ISession session = ChannelUtil.getSession(channel);
		ClientPingRequest pingRequest = ProtobufDataManager.decode(ClientPingRequest.class, content.byteBuffer());
		session.sendMessage(ServerPongResponse.valueOf(pingRequest.getBytes()));
		IMessageActor actor = session.getAttachObj(ServerConstants.MESSAGE_ACTOR_KEY);
		if (actor != null && actor instanceof PlayerActor playerActor && actor.isAuth()){
			playerActor.fireEvent(ClientPingEvent.valueOf(channel.attr(ServerConstants.HANDLER_TYPE_KEY).get()));
		}
		return true;
	}

	/**
	 * 正式处理handler
	 * @param session
	 * @param handler
	 * @param content
	 */
	public static void processHandler(ISession session, IHandler handler, MessageContent content) {
		IMessageActor messageActor = session.getAttachObj(ServerConstants.MESSAGE_ACTOR_KEY);

		if (handler instanceof ITransmitHandler && messageActor instanceof ICrossStatusActor && ((ICrossStatusActor) messageActor).isCrossStatus()) {
			DefaultByteBufMessage message = DefaultByteBufMessage.valueOf(content.getProtocolId(), content.byteBuf());
			// 回收content. 并防止里面的byteBuf被回收.
			content.recycle();
			messageActor.addMessage(m -> {
				try {
					ISession crossSession = ((ICrossStatusActor) m).currentCrossSession();
					if (logger.isInfoEnabled()) {
						Class<? extends IChannelData> aClass = ChannelDataMapping.protocolClass(message.getProtocolID());
						if (! aClass.isAnnotationPresent(SkipDebugOut.class)) {
							IChannelData channelData = ProtobufDataManager.decode(aClass, message.byteBuffer());
							logger.info("[{}] transmit {} data: {}", crossSession, session.getAttachObj(ServerConstants.HANDLER_TYPE_KEY), channelData._toString());
						}
					}
					((ICrossStatusActor) messageActor).sendCrossMessage(message);
				}catch (Exception e) {
					if (message.getContent() != null && message.getContent().refCnt() > 0) {
						message.getContent().release();
					}
				}
			});
			return;
		}
		if (session.isActive()) {
			IRequestContext context = handler.getDataType().createRequestContext(session, content);
			messageActor.addMessage((IMessage) context);
		}
	}

	public static void sendHttpResponseStatusAndClose(HttpSession session, HttpResponseStatus status) {
		FullHttpResponse response = new DefaultFullHttpResponse(HTTP_1_1, status);
		session.channel().writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);
		session.channel().close();
	}

	public static void sendHttpResponseStatusAndClose(ChannelHandlerContext ctx, HttpResponseStatus status) {
		sendHttpResponseStatusAndClose((HttpSession) ChannelUtil.getSession(ctx.channel()), status);
	}
}
