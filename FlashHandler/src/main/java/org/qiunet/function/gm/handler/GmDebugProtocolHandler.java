package org.qiunet.function.gm.handler;

import com.google.common.collect.Maps;
import io.netty.channel.Channel;
import org.qiunet.data.util.ServerConfig;
import org.qiunet.flash.handler.common.annotation.SkipDebugOut;
import org.qiunet.flash.handler.common.player.PlayerActor;
import org.qiunet.flash.handler.context.request.data.ChannelDataMapping;
import org.qiunet.flash.handler.context.request.data.IChannelData;
import org.qiunet.flash.handler.context.request.persistconn.IPersistConnRequest;
import org.qiunet.flash.handler.context.request.persistconn.IPersistConnRequestContext;
import org.qiunet.flash.handler.context.response.push.IChannelMessage;
import org.qiunet.flash.handler.context.status.IGameStatus;
import org.qiunet.flash.handler.context.status.StatusResultException;
import org.qiunet.flash.handler.handler.IHandler;
import org.qiunet.flash.handler.handler.persistconn.IPersistConnHandler;
import org.qiunet.flash.handler.handler.persistconn.PersistConnPbHandler;
import org.qiunet.flash.handler.netty.server.constants.CloseCause;
import org.qiunet.flash.handler.netty.server.constants.ServerConstants;
import org.qiunet.flash.handler.util.ChannelUtil;
import org.qiunet.function.gm.proto.req.GmDebugProtocolReq;
import org.qiunet.function.gm.proto.rsp.GmDebugProtocolRsp;
import org.qiunet.utils.json.JsonUtil;
import org.qiunet.utils.logger.LoggerType;
import org.qiunet.utils.string.ToString;
import org.slf4j.Logger;

import java.util.Map;

/***
 * 调试协议
 *
 * @author qiunet
 * 2021/12/28 09:45
 */
public class GmDebugProtocolHandler extends PersistConnPbHandler<PlayerActor, GmDebugProtocolReq> {
	@Override
	public void handler(PlayerActor playerActor, IPersistConnRequest<GmDebugProtocolReq> context) throws Exception {
		if (ServerConfig.isOfficial()) {
			LoggerType.DUODUO.error("Current is official server. can not access gm command!");
			return;
		}

		int protocolID = context.getRequestData().getProtocolId();
		String data = context.getRequestData().getData();
		Class<? extends IChannelData> aClass = ChannelDataMapping.protocolClass(protocolID);
		if (aClass == null) {
			throw StatusResultException.valueOf(IGameStatus.FAIL);
		}

		IHandler<IChannelData> handler = ChannelDataMapping.getHandler(protocolID);
		if (handler == null || ! IPersistConnHandler.class.isAssignableFrom(handler.getClass())) {
			throw StatusResultException.valueOf(IGameStatus.FAIL);
		}


		IChannelData channelData = JsonUtil.getGeneralObjWithField(data, aClass);

		ChannelDataMapping.requestCheck(context.channel(), channelData);

		GmRequestContext<IChannelData> debugContext = new GmRequestContext<>(playerActor, channelData, protocolID);
		((IPersistConnHandler<PlayerActor, IChannelData>) handler).handler(playerActor, new IPersistConnRequest<IChannelData>() {
			@Override
			public Object getAttribute(String key) {
				return debugContext.getAttribute(key);
			}

			@Override
			public void setAttribute(String key, Object val) {
				debugContext.setAttribute(key, val);
			}

			@Override
			public IChannelData getRequestData() {
				return debugContext.getRequestData();
			}

			@Override
			public String getRemoteAddress() {
				return debugContext.getRemoteAddress();
			}

			@Override
			public Channel channel() {
				return debugContext.channel();
			}
		});
		// 上面throw exception 不会执行下面.
		playerActor.sendMessage(GmDebugProtocolRsp.valueOf());
	}


	/***
	 *
	 * @author qiunet
	 * 2021/11/9 18:05
	 */
	private static class GmRequestContext<RequestData> implements IPersistConnRequestContext<RequestData, PlayerActor> {
		protected Logger logger = LoggerType.DUODUO_FLASH_HANDLER.getLogger();

		private Map<String , Object> attributes;
		// 请求数据. 必须在这里获取. 然后release MessageContent
		private final RequestData requestData;

		private final PlayerActor messageActor;

		private final int protocolID;

		public GmRequestContext(PlayerActor messageActor, RequestData requestData, int protocolID) {
			this.messageActor = messageActor;
			this.requestData = requestData;
			this.protocolID = protocolID;
		}

		@Override
		public IHandler<RequestData> getHandler() {
			return ChannelDataMapping.getHandler(protocolID);
		}

		@Override
		public void handlerRequest() throws Exception {
			if (getHandler().needAuth() && ! messageActor.isAuth()) {
				messageActor.getSender().close(CloseCause.ERR_REQUEST);
				return;
			}

			if (logger.isInfoEnabled() && ! requestData.getClass().isAnnotationPresent(SkipDebugOut.class)) {
				logger.info("[{}] <<< {}", messageActor.getIdentity(), ToString.toString(getRequestData()));
			}


			((IPersistConnHandler) getHandler()).handler(messageActor, this);
		}

		@Override
		public RequestData getRequestData() {
			return requestData;
		}

		@Override
		public String getRemoteAddress() {
			return ChannelUtil.getIp(messageActor.getSender().channel());
		}

		@Override
		public Object getAttribute(String key) {
			if (attributes == null) {
				return null;
			}
			return attributes.get(key);
		}

		@Override
		public void setAttribute(String key, Object val) {
			if (attributes == null) {
				attributes = Maps.newConcurrentMap();
			}
			attributes.put(key, val);
		}

		@Override
		public void execute(PlayerActor playerActor) {
			try {
				this.handlerRequest();
			}catch (Exception e) {
				IChannelMessage<IChannelData> protobufMessage = channel().attr(ServerConstants.HANDLER_PARAM_KEY).get().getStartupContext().exception(e);
				messageActor.sendMessage(protobufMessage);
			}
		}

		@Override
		public Channel channel() {
			return messageActor.getSender().channel();
		}
	}
}
