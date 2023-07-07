package org.qiunet.flash.handler.common.player.offline;

import io.netty.channel.ChannelFuture;
import org.qiunet.flash.handler.common.player.PlayerActor;
import org.qiunet.flash.handler.common.player.event.OfflineUserCreateEvent;
import org.qiunet.flash.handler.common.player.event.OfflineUserDestroyEvent;
import org.qiunet.flash.handler.context.response.push.IChannelMessage;
import org.qiunet.flash.handler.context.session.ISession;
import org.qiunet.utils.exceptions.CustomException;

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
		super(null);
		this.playerId = playerId;
		this.setMsgExecuteIndex(String.valueOf(playerId));
		this.fireEvent(OfflineUserCreateEvent.valueOf(this));
	}
	@Override
	public void crossToServer(int serverId) {
		throw new CustomException("Offline player Actor can not cross to other server!");
	}

	@Override
	public boolean isOnlineActor() {
		return false;
	}

	@Override
	public void auth(long id) {}

	@Override
	public void loginSuccess() {}

	@Override
	public ChannelFuture sendMessage(IChannelMessage<?> message, boolean flush) {
		throw new CustomException("Can not send message use offline player actor");
	}

	@Override
	public ChannelFuture sendKcpMessage(IChannelMessage<?> message, boolean flush) {
		throw new CustomException("Can not send message use offline player actor");
	}

	@Override
	public ISession getKcpSession() {
		return null;
	}

	@Override
	public void logout() {
		// do nothing
	}

	public void destroy(){
		if (isDestroyed()) {
			return;
		}

		this.fireEvent(OfflineUserDestroyEvent.valueOf());
		UserOfflineManager.instance.remove(getPlayerId());
		dataLoader().unregister();
	}
}
