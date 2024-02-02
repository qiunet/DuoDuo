package org.qiunet.flash.handler.common.player.connect;

import com.google.common.collect.Maps;
import io.netty.channel.Channel;
import io.netty.channel.pool.ChannelPool;
import org.qiunet.cross.pool.NodeChannelPoolMap;
import org.qiunet.cross.pool.NodeChannelTrigger;
import org.qiunet.data.util.ServerConfig;
import org.qiunet.flash.handler.common.annotation.SkipDebugOut;
import org.qiunet.flash.handler.common.enums.ServerConnType;
import org.qiunet.flash.handler.common.message.MessageContent;
import org.qiunet.flash.handler.common.player.IMessageActor;
import org.qiunet.flash.handler.common.player.PlayerActor;
import org.qiunet.flash.handler.common.player.event.CrossChannelErrorEvent;
import org.qiunet.flash.handler.common.player.event.CrossPlayerDestroyEvent;
import org.qiunet.flash.handler.common.player.event.PlayerQuitCrossEvent;
import org.qiunet.flash.handler.common.protobuf.ProtobufDataManager;
import org.qiunet.flash.handler.context.header.INodeServerHeader;
import org.qiunet.flash.handler.context.request.data.ChannelDataMapping;
import org.qiunet.flash.handler.context.request.data.IChannelData;
import org.qiunet.flash.handler.context.request.data.InterestedChannelData;
import org.qiunet.flash.handler.context.response.push.DefaultByteBufMessage;
import org.qiunet.flash.handler.context.response.push.DefaultBytesMessage;
import org.qiunet.flash.handler.context.session.ISession;
import org.qiunet.flash.handler.context.session.NodeClientSession;
import org.qiunet.flash.handler.context.session.kcp.IKcpSessionHolder;
import org.qiunet.flash.handler.netty.server.constants.CloseCause;
import org.qiunet.flash.handler.netty.server.constants.ServerConstants;
import org.qiunet.utils.data.ByteUtil;
import org.qiunet.utils.listener.event.EventHandlerWeightType;
import org.qiunet.utils.listener.event.EventListener;
import org.qiunet.utils.listener.event.data.ServerShutdownEvent;
import org.qiunet.utils.logger.LoggerType;
import org.slf4j.Logger;

import java.util.Collections;
import java.util.Map;

/***
 * 游戏服跨服session 管理
 *
 * @author qiunet
 * 2020-10-23 17:44
 */
enum CrossSessionManager implements NodeChannelTrigger {
	instance;
	private static final Logger logger = LoggerType.DUODUO_FLASH_HANDLER.getLogger();
	/**
	 * poolMap
	 */
	private final NodeChannelPoolMap poolMap = new NodeChannelPoolMap(this);
	/**
	 * session id 映射关系
	 */
	private final Map<Integer, Map<Long, NodeClientSession>> sessionMap = Maps.newConcurrentMap();
	/**
	 * 新的Session 移除
	 * @param playerId
	 * @param session
	 */
	void addNewSession(long playerId, int serverId, NodeClientSession session) {
		Map<Long, NodeClientSession> map = sessionMap.computeIfAbsent(serverId, key -> Maps.newConcurrentMap());
		session.addCloseListener("connectorQuitCross", ((session1, cause) -> {
			NodeClientSession clientSession = (NodeClientSession) session1;
			if (! clientSession.isNoticedRemote()) {
				PlayerCrossConnector playerCrossConnector = PlayerCrossConnector.get(clientSession);
				playerCrossConnector.fireCrossEvent(new PlayerQuitCrossEvent());
				clientSession.setNoticedRemote();
			}

			LoggerType.DUODUO_FLASH_HANDLER.info("Player CrossSession Id {} serverId {}  be removed! ", clientSession.getId(), serverId);
			sessionMap.getOrDefault(serverId, Collections.emptyMap()).remove(((NodeClientSession)session1).getId());
		}));

		map.put(playerId, session);
	}

	@Override
	public ISession getNodeSession(Channel channel, INodeServerHeader header) {
		return getNodeSession(header.getServerId(), header.id());
	}

	public ISession getNodeSession(int serverId, long playerId) {
		Map<Long, NodeClientSession> map = sessionMap.get(serverId);
		if (map == null) {
			return null;
		}
		return map.get(playerId);
	}

	/**
	 * 获得池资源
	 * @param serverId
	 * @return
	 */
	ChannelPool getChannelPool(int serverId) {
		return this.poolMap.get(serverId);
	}

	@EventListener
	private void shutdown(ServerShutdownEvent event) {
		sessionMap.forEach((serverId, map) -> {
			map.values().forEach(s -> s.close(CloseCause.SERVER_SHUTDOWN));
		});
	}

	@EventListener
	private void crossChannelErrorEvent(CrossChannelErrorEvent event) {
		event.getPlayer().getSession().close(event.getCause());
	}

	@EventListener(EventHandlerWeightType.LOWEST)
	private void crossDestroy(CrossPlayerDestroyEvent event) {
		ISession nodeSession = getNodeSession(event.getServerId(), event.getPlayer().getPlayerId());
		if (nodeSession != null) {
			((NodeClientSession) nodeSession).setNoticedRemote();
			nodeSession.close(CloseCause.DESTROY);
		}
	}

	@Override
	public void response0(ISession session, Channel channel, MessageContent data) {
		PlayerActor playerActor = (PlayerActor) session.getAttachObj(ServerConstants.MESSAGE_ACTOR_KEY);
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
		INodeServerHeader header = (INodeServerHeader) data.getHeader();
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
		ISession session = iMessageActor.getSession();
		if (logger.isInfoEnabled()) {
			Class<? extends IChannelData> aClass = ChannelDataMapping.protocolClass(message.getProtocolID());
			if (! aClass.isAnnotationPresent(SkipDebugOut.class)|| ServerConfig.isDebugEnv()) {
                IChannelData channelData;
                try {
                    channelData = ProtobufDataManager.decode(aClass, message.byteBuffer());
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
                ServerConnType serverConnType = session.getAttachObj(ServerConstants.HANDLER_TYPE_KEY);
				logger.info("{} C2P {} message: {}", iMessageActor.getIdentity(), kcp  ? "KCP": serverConnType, channelData._toString());
			}
		}
		if (kcp
		&& IKcpSessionHolder.class.isAssignableFrom(session.getClass())
		&& ((IKcpSessionHolder)session).isKcpSessionPrepare()) {
			((IKcpSessionHolder) session).sendKcpMessage(message, flush);
		}else {
			iMessageActor.getSession().sendMessage(message, flush);
		}
	}
}
