package org.qiunet.flash.handler.common.player.offline;

import org.qiunet.data.db.loader.IPlayerDataLoader;
import org.qiunet.data.db.loader.PlayerDataLoader;
import org.qiunet.flash.handler.common.MessageHandler;
import org.qiunet.flash.handler.common.player.IPlayer;
import org.qiunet.flash.handler.common.player.event.OfflineUserCreateEvent;
import org.qiunet.flash.handler.common.player.event.OfflineUserDestroyEvent;
import org.qiunet.flash.handler.common.player.event.UserEventData;

/***
 * 离线玩家actor
 * 仅仅是读取数据使用.
 * 不使用了需要destroy
 *
 * @author qiunet
 * 2021/11/19 11:55
 */
public class OfflinePlayerActor extends MessageHandler<OfflinePlayerActor> implements IPlayerDataLoader, IPlayer {
	/**
	 * 玩家的数据加载器
	 */
	private final PlayerDataLoader dataLoader;

	OfflinePlayerActor(long playerId) {
		this.dataLoader = new PlayerDataLoader(playerId);
		this.fireEvent(OfflineUserCreateEvent.valueOf());
	}

	public long getPlayerId() {
		return this.dataLoader.getPlayerId();
	}

	@Override
	public PlayerDataLoader dataLoader() {
		return dataLoader;
	}

	@Override
	public long getId() {
		return getPlayerId();
	}

	/**
	 * 触发事件
	 * @param eventData
	 */
	public void fireEvent(UserEventData eventData) {
		eventData.setPlayer(this);
		this.addMessage(a -> eventData.fireEventHandler());
	}

	public void destroy(){
		if (isDestroyed()) {
			return;
		}

		super.destroy();

		this.fireEvent(OfflineUserDestroyEvent.valueOf());

		UserOfflineManager.instance.remove(getPlayerId());
		this.dataLoader.unregister();

	}
}
