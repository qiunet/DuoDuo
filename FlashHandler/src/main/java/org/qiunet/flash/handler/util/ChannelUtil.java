package org.qiunet.flash.handler.util;

import com.google.common.base.Preconditions;
import io.jpower.kcp.netty.KcpException;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.util.Attribute;
import org.qiunet.flash.handler.common.annotation.SkipDebugOut;
import org.qiunet.flash.handler.common.id.IProtocolId;
import org.qiunet.flash.handler.common.message.MessageContent;
import org.qiunet.flash.handler.common.player.AbstractMessageActor;
import org.qiunet.flash.handler.common.player.ICrossStatusActor;
import org.qiunet.flash.handler.common.player.IMessageActor;
import org.qiunet.flash.handler.common.player.PlayerActor;
import org.qiunet.flash.handler.common.protobuf.ProtobufDataManager;
import org.qiunet.flash.handler.context.header.IProtocolHeaderType;
import org.qiunet.flash.handler.context.request.data.ChannelDataMapping;
import org.qiunet.flash.handler.context.request.data.IChannelData;
import org.qiunet.flash.handler.context.request.persistconn.IPersistConnRequestContext;
import org.qiunet.flash.handler.context.response.push.DefaultByteBufMessage;
import org.qiunet.flash.handler.context.session.ISession;
import org.qiunet.flash.handler.handler.IHandler;
import org.qiunet.flash.handler.netty.server.constants.CloseCause;
import org.qiunet.flash.handler.netty.server.constants.ServerConstants;
import org.qiunet.flash.handler.netty.server.kcp.observer.IKcpUsabilityChange;
import org.qiunet.flash.handler.netty.server.message.ConnectionReq;
import org.qiunet.flash.handler.netty.server.message.ConnectionRsp;
import org.qiunet.flash.handler.netty.server.param.AbstractBootstrapParam;
import org.qiunet.flash.handler.netty.server.param.KcpBootstrapParams;
import org.qiunet.flash.handler.netty.server.param.adapter.IStartupContext;
import org.qiunet.flash.handler.netty.server.param.adapter.message.ClientPingRequest;
import org.qiunet.flash.handler.netty.server.param.adapter.message.ServerPongResponse;
import org.qiunet.flash.handler.netty.transmit.ITransmitHandler;
import org.qiunet.utils.logger.LoggerType;
import org.qiunet.utils.string.StringUtil;
import org.qiunet.utils.string.ToString;
import org.slf4j.Logger;

import java.io.IOException;
import java.net.InetSocketAddress;

import static io.netty.handler.codec.http.HttpVersion.HTTP_1_1;

public final class ChannelUtil {
	private static final Logger logger = LoggerType.DUODUO_FLASH_HANDLER.getLogger();
	private ChannelUtil(){}
	/***
	 * 得到channel保存的ProtocolHeader数据
	 * @param channel
	 * @return
	 */
	public static IProtocolHeaderType getProtocolHeaderAdapter(Channel channel) {
		return channel.attr(ServerConstants.PROTOCOL_HEADER_ADAPTER).get();
	}
	/***
	 * 关联Session 和 channel
	 * @param val
	 * @return
	 */
	public static boolean bindSession(ISession val) {
		Preconditions.checkNotNull(val);
		Attribute<ISession> attr = val.channel().attr(ServerConstants.SESSION_KEY);
		boolean result = attr.compareAndSet(null, val);
		if (! result) {
			logger.error("Session [{}] Duplicate", val);
			val.close(CloseCause.LOGIN_REPEATED);
		}
		return result;
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
		return getIp(channel.attr(ServerConstants.HTTP_WS_HEADER_KEY).get(), channel);
	}

	/**
	 * 得到真实ip. http类型的父类
	 * @param headers
	 * @return
	 */
	public static String getIp(HttpHeaders headers, Channel channel) {
		String ip;
		if (headers != null) {
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
		}
		if (channel.remoteAddress() == null) {
			return "unknown-address";
		}

		return ((InetSocketAddress) channel.remoteAddress()).getAddress().getHostAddress();
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

		ClientPingRequest pingRequest = ProtobufDataManager.decode(ClientPingRequest.class, content.byteBuffer());
		ChannelUtil.getSession(channel).sendMessage(ServerPongResponse.valueOf(pingRequest.getBytes()));
		return true;
	}

	/**
	 * 处理长连接的通道读数据
	 * @param channel
	 * @param params
	 * @param content
	 */
	public static void channelRead(Channel channel, AbstractBootstrapParam params, MessageContent content){
		ISession session = ChannelUtil.getSession(channel);
		Preconditions.checkNotNull(session);

		if (! params.getStartupContext().userServerValidate(session)) {
			return;
		}

		if (content.getProtocolId() == IProtocolId.System.CONNECTION_REQ) {
			boolean isKcp = params instanceof KcpBootstrapParams;
			if (isKcp && ((KcpBootstrapParams) params).isDependOnTcpWs()) {
				// 不需要
				return;
			}

			AbstractMessageActor messageActor = (AbstractMessageActor) session.getAttachObj(ServerConstants.MESSAGE_ACTOR_KEY);
			ConnectionReq connectionReq = ProtobufDataManager.decode(ConnectionReq.class, content.byteBuffer());
			if (logger.isInfoEnabled()) {
				logger.info("[{}] [{}({})] <<< {}", messageActor.getIdentity(), channel.attr(ServerConstants.HANDLER_TYPE_KEY).get(), channel.id().asShortText(), ToString.toString(connectionReq));
			}

			if (StringUtil.isEmpty(connectionReq.getIdKey())) {
				logger.info("ConnectionReq idKey is null!");
				channel.close();
				return;
			}

			messageActor.setMsgExecuteIndex(connectionReq.getIdKey());
			messageActor.sendMessage(ConnectionRsp.getInstance());

			if (isKcp) {
				((PlayerActor) messageActor).asyncFireObserver(IKcpUsabilityChange.class, o -> o.ability(true));
			}
			return;
		}


		if (session.getAttachObj(ServerConstants.MESSAGE_ACTOR_KEY).msgExecuteIndex() == null) {
			logger.info("MessageActor msgExecuteIndex is null! Need call ConnectionReq first");
			channel.close();
			return;
		}

		IHandler handler = ChannelDataMapping.getHandler(content.getProtocolId());
		if (handler == null) {
			channel.writeAndFlush(params.getStartupContext().getHandlerNotFound());
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
			content.retain();
			messageActor.addMessage(m -> transmitMessage(messageActor, content, channel));
			return;
		}
		if (channel.isActive()) {
			IPersistConnRequestContext context = handler.getDataType().createPersistConnRequestContext(content, channel, handler, messageActor);
			messageActor.addMessage(context);
		}
	}


	private static void transmitMessage(IMessageActor messageActor, MessageContent content, Channel channel) {
		if (logger.isInfoEnabled()) {
			Class<? extends IChannelData> aClass = ChannelDataMapping.protocolClass(content.getProtocolId());
			if (! aClass.isAnnotationPresent(SkipDebugOut.class)) {
				IChannelData channelData = ProtobufDataManager.decode(aClass, content.byteBuffer());
				logger.info("[{}] transmit {} data: {}", messageActor.getIdentity(), channel.attr(ServerConstants.HANDLER_TYPE_KEY).get(), ToString.toString(channelData));
			}
		}

		DefaultByteBufMessage bufferMessage = new DefaultByteBufMessage(content.getProtocolId(), content.byteBuf());
		ISession crossSession = ((ICrossStatusActor) messageActor).crossSession();
		crossSession.sendMessage(bufferMessage);
	}

	public static void sendHttpResponseStatusAndClose(Channel channel, HttpResponseStatus status) {
		FullHttpResponse response = new DefaultFullHttpResponse(HTTP_1_1, status);
		channel.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);
		channel.close();
	}

	public static void sendHttpResponseStatusAndClose(ChannelHandlerContext ctx, HttpResponseStatus status) {
		sendHttpResponseStatusAndClose(ctx.channel(), status);
	}

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

		if (channel.isOpen() || channel.isActive()) {
			startupContext.exception(channel, cause)
					.addListener(f -> {
						closeChannel.run();
					});
		}
	}
}
