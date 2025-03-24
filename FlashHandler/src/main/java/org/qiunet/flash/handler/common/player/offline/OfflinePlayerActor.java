package org.qiunet.flash.handler.common.player.offline;

import io.netty.channel.ChannelFuture;
import org.qiunet.flash.handler.common.player.PlayerActor;
import org.qiunet.flash.handler.common.player.event.OfflineUserCreateEvent;
import org.qiunet.flash.handler.common.player.event.OfflineUserDestroyEvent;
import org.qiunet.flash.handler.context.response.push.IChannelMessage;
import org.qiunet.flash.handler.context.session.ISession;
import org.qiunet.utils.exceptions.CustomException;

import java.util.function.Consumer;

/***
 * 离线玩家actor
 * 仅仅是读取数据使用.
 * 不使用了需要destroy
 *
 * @author qiunet
 * 2021/11/19 11:55
 */
public class OfflinePlayerActor extends PlayerActor {

	OfflinePlayerActor(long playerId) {
		super(null, String.valueOf(playerId));
		this.playerId = playerId;
		dataLoader().setOffline(true);
		this.fireEvent(OfflineUserCreateEvent.valueOf(this));
	}

	@Override
	public void crossToServer(int serverId, String msgQueueIndex, Consumer<Boolean> resultCallback) {
		throw new CustomException("Offline player Actor can not cross to other server!");
	}

	@Override
	public boolean isOnlineActor() {
		return false;
	}

	@Override
	public void auth(long id) {}

	@Override
	public boolean loginSuccess() {
		return true;
	}

	@Override
	public ChannelFuture sendMessage(IChannelMessage<?> message, boolean flush) {
			return this.sendKcpMessage(message, flush);
		}

	@Override
	public ChannelFuture sendKcpMessage(IChannelMessage<?> message, boolean flush) {
		logger.error("PlayerID {} can not send message use offline player actor", playerId);
		return null;
	}

	@Override
	public ISession getKcpSession() {
		return null;
	}

	@Override
	public void logout() {
		// do nothing
	}

	void remove() {
		this.fireEvent(OfflineUserDestroyEvent.valueOf());
		this.addMessage(a -> ((OfflinePlayerActor) a).destroy());
		this.destroy();
	}

	public void destroy(){
		if (isDestroyed()) {
			return;
		}

		dataLoader().unregister();
		super.destroy();
	}
}
