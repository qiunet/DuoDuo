package org.qiunet.function.gm.handler;

import io.netty.buffer.ByteBuf;
import org.qiunet.data.util.ServerConfig;
import org.qiunet.flash.handler.common.message.MessageContent;
import org.qiunet.flash.handler.common.player.PlayerActor;
import org.qiunet.flash.handler.context.header.IProtocolHeader;
import org.qiunet.flash.handler.context.request.data.ChannelDataMapping;
import org.qiunet.flash.handler.context.request.data.IChannelData;
import org.qiunet.flash.handler.context.request.persistconn.IPersistConnRequest;
import org.qiunet.flash.handler.context.status.IGameStatus;
import org.qiunet.flash.handler.context.status.StatusResultException;
import org.qiunet.flash.handler.handler.IHandler;
import org.qiunet.flash.handler.handler.persistconn.IPersistConnHandler;
import org.qiunet.flash.handler.handler.persistconn.PersistConnPbHandler;
import org.qiunet.flash.handler.util.ChannelUtil;
import org.qiunet.function.gm.proto.req.GmDebugProtocolReq;
import org.qiunet.function.gm.proto.rsp.GmDebugProtocolRsp;
import org.qiunet.utils.async.LazyLoader;
import org.qiunet.utils.json.JsonUtil;
import org.qiunet.utils.logger.LoggerType;

import java.nio.ByteBuffer;

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
		final LazyLoader<ByteBuf> bufferLazyLoader = new LazyLoader<>(channelData::toByteBuf);
		ChannelUtil.processHandler(context.channel(), handler, MessageContent.valueOf(new IProtocolHeader() {
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
			public boolean isMagicValid() {
				return true;
			}

			@Override
			public int getLength() {
				return bufferLazyLoader.get().readableBytes();
			}

			@Override
			public boolean validEncryption(ByteBuffer buffer) {
				return true;
			}
		}, bufferLazyLoader.get()));
		// 上面throw exception 不会执行下面.
		playerActor.sendMessage(GmDebugProtocolRsp.valueOf());
	}

}
