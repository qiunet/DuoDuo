package org.qiunet.cross.test.common.actor;

import org.qiunet.cross.actor.auth.CrossPlayerAuthRequest;
import org.qiunet.cross.common.trigger.TcpNodeClientTrigger;
import org.qiunet.cross.event.BaseCrossPlayerEventData;
import org.qiunet.cross.event.CrossEventManager;
import org.qiunet.cross.node.ServerInfo;
import org.qiunet.cross.node.ServerNodeManager;
import org.qiunet.data.util.ServerConfig;
import org.qiunet.data.util.ServerType;
import org.qiunet.flash.handler.common.player.AbstractPlayerActor;
import org.qiunet.flash.handler.common.player.event.AuthEventData;
import org.qiunet.flash.handler.context.session.DSession;
import org.qiunet.flash.handler.netty.client.param.TcpClientParams;
import org.qiunet.flash.handler.netty.client.tcp.NettyTcpClient;
import org.qiunet.flash.handler.netty.server.constants.ServerConstants;
import org.qiunet.utils.listener.event.EventManager;
import org.qiunet.utils.logger.LoggerType;
import org.slf4j.Logger;

import java.util.concurrent.atomic.AtomicBoolean;

/***
 *
 *
 * @author qiunet
 * 2020-10-22 21:32
 */
public class PlayerActor extends AbstractPlayerActor<PlayerActor> {
	private static final NettyTcpClient tcpClient = NettyTcpClient.create(TcpClientParams.DEFAULT_PARAMS, new TcpNodeClientTrigger());
	private final Logger logger = LoggerType.DUODUO_CROSS.getLogger();
	/**跨服标记*/
	private final AtomicBoolean crossing = new AtomicBoolean();

	private DSession crossSession;

	private long playerId;

	public PlayerActor(DSession session) {
		super(session);

		session.addCloseListener(cause -> crossing.set(false));
	}

	public boolean cross(int serverId) {
		if (isCrossStatus()) {
			return false;
		}
		ServerInfo serverInfo = ServerNodeManager.getServerInfo(serverId);
		if (serverInfo.getType() != ServerType.CROSS) {
			logger.error("ServerId {} is not a cross server!", serverInfo.getType());
			return false;
		}

		this.crossSession = tcpClient.connect("localhost", serverInfo.getServerPort(), f ->
				f.channel().attr(ServerConstants.MESSAGE_ACTOR_KEY).set(this));
		this.crossSession.sendMessage(CrossPlayerAuthRequest.valueOf(getId(), ServerConfig.getServerId()));
		this.crossSession.addCloseListener(cause -> this.crossing.set(false));
		return this.crossing.compareAndSet(false, true);
	}

	public <D extends BaseCrossPlayerEventData> void fireCrossEvent(D eventData) {
		CrossEventManager.fireCrossEvent(getId(), this.crossSession, eventData);
	}

	@Override
	public void auth(long playerId) {
		this.playerId = playerId;
		EventManager.fireEventHandler(new AuthEventData<>(this));
	}

	@Override
	public boolean isCrossStatus() {
		return crossing.get();
	}

	@Override
	public DSession crossSession() {
		return crossSession;
	}


	@Override
	public long getId() {
		return playerId;
	}
}
