package org.qiunet.function.gm.handler;

import io.netty.channel.Channel;
import org.qiunet.data.util.ServerConfig;
import org.qiunet.flash.handler.common.annotation.SkipDebugOut;
import org.qiunet.flash.handler.common.player.PlayerActor;
import org.qiunet.flash.handler.context.request.data.ChannelDataMapping;
import org.qiunet.flash.handler.context.request.data.IChannelData;
import org.qiunet.flash.handler.context.request.persistconn.IPersistConnRequest;
import org.qiunet.flash.handler.context.status.IGameStatus;
import org.qiunet.flash.handler.context.status.StatusResultException;
import org.qiunet.flash.handler.handler.IHandler;
import org.qiunet.flash.handler.handler.persistconn.IPersistConnHandler;
import org.qiunet.flash.handler.handler.persistconn.PersistConnPbHandler;
import org.qiunet.function.gm.proto.req.GmDebugProtocolReq;
import org.qiunet.function.gm.proto.rsp.GmDebugProtocolRsp;
import org.qiunet.utils.json.JsonUtil;
import org.qiunet.utils.logger.LoggerType;
import org.qiunet.utils.string.ToString;

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
		if (logger.isInfoEnabled() && ! aClass.isAnnotationPresent(SkipDebugOut.class)) {
			logger.info("[{}] <<< {}", playerActor.getIdentity(), ToString.toString(channelData));
		}

		ChannelDataMapping.requestCheck(context.channel(), channelData);
		((IPersistConnHandler<PlayerActor, IChannelData>) handler).handler(playerActor, new IPersistConnRequest<IChannelData>() {
			@Override
			public Object getAttribute(String key) {
				return context.getAttribute(key);
			}

			@Override
			public void setAttribute(String key, Object val) {
				context.setAttribute(key, val);
			}

			@Override
			public IChannelData getRequestData() {
				return channelData;
			}

			@Override
			public String getRemoteAddress() {
				return context.getRemoteAddress();
			}

			@Override
			public Channel channel() {
				return context.channel();
			}
		});
		// 上面throw exception 不会执行下面.
		playerActor.sendMessage(GmDebugProtocolRsp.valueOf());
	}

}
