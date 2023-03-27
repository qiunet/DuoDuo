package org.qiunet.flash.handler.util;

import com.google.common.base.Preconditions;
import io.jpower.kcp.netty.KcpException;
import io.micrometer.core.instrument.Counter;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.util.Attribute;
import org.qiunet.flash.handler.common.IMessage;
import org.qiunet.flash.handler.common.annotation.SkipDebugOut;
import org.qiunet.flash.handler.common.enums.ServerConnType;
import org.qiunet.flash.handler.common.event.ClientPingEvent;
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
import org.qiunet.flash.handler.context.response.push.IChannelMessage;
import org.qiunet.flash.handler.context.session.ISession;
import org.qiunet.flash.handler.context.status.StatusResultException;
import org.qiunet.flash.handler.handler.IHandler;
import org.qiunet.flash.handler.netty.server.config.ServerBootStrapConfig;
import org.qiunet.flash.handler.netty.server.config.adapter.IStartupContext;
import org.qiunet.flash.handler.netty.server.config.adapter.message.ClientPingRequest;
import org.qiunet.flash.handler.netty.server.config.adapter.message.HandlerNotFoundResponse;
import org.qiunet.flash.handler.netty.server.config.adapter.message.ServerPongResponse;
import org.qiunet.flash.handler.netty.server.constants.CloseCause;
import org.qiunet.flash.handler.netty.server.constants.ServerConstants;
import org.qiunet.flash.handler.netty.server.message.ConnectionReq;
import org.qiunet.flash.handler.netty.server.message.ConnectionRsp;
import org.qiunet.flash.handler.netty.transmit.ITransmitHandler;
import org.qiunet.function.prometheus.RootRegistry;
import org.qiunet.utils.logger.LoggerType;
import org.qiunet.utils.string.StringUtil;
import org.slf4j.Logger;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;

import static io.netty.handler.codec.http.HttpVersion.HTTP_1_1;

public final class ChannelUtil {
	private static final IChannelData HANDLER_NOT_FOUND = new HandlerNotFoundResponse();
	private static final Logger logger = LoggerType.DUODUO_FLASH_HANDLER.getLogger();
	private ChannelUtil(){}
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
	 * @return
	 */
	public static boolean bindSession(ISession session, Channel channel) {
		Preconditions.checkNotNull(session);
		Attribute<ISession> attr = channel.attr(ServerConstants.SESSION_KEY);
		boolean result = attr.compareAndSet(null, session);
		if (! result) {
			logger.error("Session [{}] Duplicate", session);
			session.close(CloseCause.LOGIN_REPEATED);
		}
		return result;
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
	 *  获得ip
	 * @return
	 */
	public static String getIp(Channel channel) {

		SocketAddress socketAddress = channel.remoteAddress();

		if (socketAddress == null) {
			return "unknown-address";
		}

		return ((InetSocketAddress) socketAddress).getAddress().getHostAddress();
	}
	/**
	 *  获得ip
	 * @return
	 */
	public static String getIp(HttpHeaders headers) {
		if (headers == null) {
			return null;
		}

		String ip;
		if (!StringUtil.isEmpty(ip = headers.get("x-forwarded-for")) && !"unknown".equalsIgnoreCase(ip)) {
			return ip;
		}

		if (! StringUtil.isEmpty(ip = headers.get("HTTP_X_FORWARDED_FOR")) && ! "unknown".equalsIgnoreCase(ip)) {
			return ip;
		}

		if (!StringUtil.isEmpty(ip = headers.get("x-forwarded-for-pound")) &&! "unknown".equalsIgnoreCase(ip)) {
			return ip;
		}

		if (!StringUtil.isEmpty(ip = headers.get("Proxy-Client-IP") ) &&! "unknown".equalsIgnoreCase(ip)) {
			return ip;
		}

		if (!StringUtil.isEmpty(ip = headers.get("WL-Proxy-Client-IP")) &&! "unknown".equalsIgnoreCase(ip)) {
			return ip;
		}
		return null;
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
		if (session.getAttachObj(ServerConstants.HANDLER_TYPE_KEY) == ServerConnType.TCP
		&& actor instanceof PlayerActor playerActor){
			playerActor.fireEvent(ClientPingEvent.getInstance());
		}
		return true;
	}

	/**
	 * 处理长连接的通道读数据
	 * @param channel
	 * @param config
	 * @param content
	 */
	public static void channelRead(Channel channel, ServerBootStrapConfig config, MessageContent content){
		ISession session = ChannelUtil.getSession(channel);
		Preconditions.checkNotNull(session);

		if (! config.getStartupContext().userServerValidate(session)) {
			return;
		}

		AbstractMessageActor messageActor = (AbstractMessageActor) session.getAttachObj(ServerConstants.MESSAGE_ACTOR_KEY);
		if (content.getProtocolId() == IProtocolId.System.CONNECTION_REQ) {
			boolean isKcp = session.getAttachObj(ServerConstants.HANDLER_TYPE_KEY) == ServerConnType.KCP;
			if (isKcp && config.getKcpBootstrapConfig().isDependOnTcpWs()) {
				// 不需要
				return;
			}

			ConnectionReq connectionReq = ProtobufDataManager.decode(ConnectionReq.class, content.byteBuffer());
			if (logger.isInfoEnabled()) {
				logger.info("[{}] [{}({})] <<< {}", messageActor.getIdentity(), session.getAttachObj(ServerConstants.HANDLER_TYPE_KEY), channel.id().asShortText(), connectionReq._toString());
			}

			if (StringUtil.isEmpty(connectionReq.getIdKey())) {
				logger.info("ConnectionReq idKey is null!");
				channel.close();
				return;
			}

			messageActor.setMsgExecuteIndex(connectionReq.getIdKey());
			messageActor.sendMessage(ConnectionRsp.getInstance());
			return;
		}


		if (messageActor.msgExecuteIndex() == null) {
			logger.info("{} msgExecuteIndex is null! Need call ConnectionReq first", messageActor.getIdentity());
			channel.close();
			return;
		}

		IHandler handler = ChannelDataMapping.getHandler(content.getProtocolId());
		if (handler == null) {
			channel.writeAndFlush(HANDLER_NOT_FOUND);
			return;
		}

		processHandler(channel, handler, content);
	}

	/**
	 * 正式处理handler
	 * @param channel
	 * @param handler
	 * @param content
	 */
	public static void processHandler(Channel channel, IHandler handler, MessageContent content) {
		ISession session = ChannelUtil.getSession(channel);
		IMessageActor messageActor = session.getAttachObj(ServerConstants.MESSAGE_ACTOR_KEY);
		if (handler instanceof ITransmitHandler && messageActor instanceof ICrossStatusActor && ((ICrossStatusActor) messageActor).isCrossStatus()) {
			DefaultByteBufMessage message = DefaultByteBufMessage.valueOf(content.getProtocolId(), content.byteBuf());
			messageActor.addMessage(m -> {
				try {
					transmitMessage(messageActor, message, channel);
				}catch (Exception e) {
					if (message.getContent() != null && message.getContent().refCnt() > 0) {
						message.getContent().release();
					}
				}
			});
			content.recycle();
			return;
		}
		if (channel.isActive()) {
			IRequestContext context = handler.getDataType().createRequestContext(session, content, channel);
			messageActor.addMessage((IMessage) context);
		}
	}


	private static void transmitMessage(IMessageActor messageActor, IChannelMessage message, Channel channel) {
		ISession session = messageActor.getSession();
		if (logger.isInfoEnabled()) {
			Class<? extends IChannelData> aClass = ChannelDataMapping.protocolClass(message.getProtocolID());
			if (! aClass.isAnnotationPresent(SkipDebugOut.class)) {
				IChannelData channelData = ProtobufDataManager.decode(aClass, message.byteBuffer());
				logger.info("[{}] transmit {} data: {}", messageActor.getIdentity(), session.getAttachObj(ServerConstants.HANDLER_TYPE_KEY), channelData._toString());
			}
		}

		((ICrossStatusActor) messageActor).sendCrossMessage(message);
	}

	public static void sendHttpResponseStatusAndClose(Channel channel, HttpResponseStatus status) {
		FullHttpResponse response = new DefaultFullHttpResponse(HTTP_1_1, status);
		channel.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);
		channel.close();
	}

	public static void sendHttpResponseStatusAndClose(ChannelHandlerContext ctx, HttpResponseStatus status) {
		sendHttpResponseStatusAndClose(ctx.channel(), status);
	}
	private static final Counter counter = RootRegistry.instance.counter("project.exception");
	/**
	 * 异常处理
	 * @param startupContext
	 * @param channel
	 * @param cause
	 */
	public static void cause(IStartupContext startupContext, Channel channel, Throwable cause) {
		ISession session = ChannelUtil.getSession(channel);
		String errMeg = "Exception session ["+(session != null ? session.toString(): "null")+"]";
		if (cause instanceof KcpException || cause instanceof IOException) {
			logger.error(errMeg + " errMsg: " + cause.getMessage());
		}else {
			logger.error(errMeg, cause);
		}

		Runnable closeChannel = () -> {
			if (session != null) {
				session.close(CloseCause.EXCEPTION);
			}else {
				channel.close();
			}
		};

		if ("Connection reset by peer".equals(cause.getMessage())) {
			closeChannel.run();
			return;
		}

		if (! (cause instanceof StatusResultException)) {
			counter.increment();
		}

		if (channel.isOpen() || channel.isActive()) {
			startupContext.exception(session, cause)
					.addListener(f -> {
						closeChannel.run();
					});
		}
	}
}
