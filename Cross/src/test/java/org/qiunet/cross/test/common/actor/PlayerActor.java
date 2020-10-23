package org.qiunet.cross.test.common.actor;

import org.qiunet.cross.actor.auth.CrossPlayerAuthRequest;
import org.qiunet.cross.common.trigger.TcpNodeClientTrigger;
import org.qiunet.cross.event.BaseCrossPlayerEventData;
import org.qiunet.cross.event.CrossEventManager;
import org.qiunet.cross.node.ServerInfo;
import org.qiunet.cross.node.ServerNodeManager;
import org.qiunet.flash.handler.common.player.AbstractPlayerActor;
import org.qiunet.flash.handler.common.player.event.AuthEventData;
import org.qiunet.flash.handler.context.session.DSession;
import org.qiunet.flash.handler.netty.client.param.TcpClientParams;
import org.qiunet.flash.handler.netty.client.tcp.NettyTcpClient;
import org.qiunet.listener.event.EventManager;

import java.util.concurrent.atomic.AtomicBoolean;

/***
 *
 *
 * @author qiunet
 * 2020-10-22 21:32
 */
public class PlayerActor extends AbstractPlayerActor<PlayerActor> {
	/**跨服标记*/
	private AtomicBoolean crossing = new AtomicBoolean();

	private DSession crossSession;

	private long playerId;

	public PlayerActor(DSession session) {
		super(session);

		session.addCloseListener(cause -> crossing.set(false));
	}

	public void cross(int serverId) {
		if (isCrossStatus()) {
			return;
		}
		ServerInfo serverInfo = ServerNodeManager.getServerInfo(serverId);
		new NettyTcpClient(TcpClientParams.custom()
			.setAddress("localhost", serverInfo.getServerPort())
			.build(), new TcpNodeClientTrigger(), future -> {
			this.crossSession = new DSession(future.channel());
			this.crossSession.writeMessage(CrossPlayerAuthRequest.valueOf(getId()));
			this.crossSession.addCloseListener(cause -> this.crossing.set(false));
			this.crossing.compareAndSet(false, true);
		});
	}

	public <D extends BaseCrossPlayerEventData> void fireCrossEvent(D eventData) {
		CrossEventManager.fireCrossEvent(getId(), this.crossSession, eventData);
	}

	public void auth(long playerId) {
		this.playerId = playerId;
		EventManager.fireEventHandler(new AuthEventData<>(this));
	}

	@Override
	public boolean isCrossStatus() {
		return crossing.get();
	}

	@Override
	protected String getIdent() {
		return "PlayerActor_"+getId();
	}

	@Override
	public long getId() {
		return playerId;
	}
}
