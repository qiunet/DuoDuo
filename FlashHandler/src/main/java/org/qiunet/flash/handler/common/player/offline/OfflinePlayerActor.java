package org.qiunet.flash.handler.common.player.offline;

import org.qiunet.data.db.loader.IPlayerDataLoader;
import org.qiunet.data.db.loader.PlayerDataLoader;
import org.qiunet.flash.handler.common.MessageHandler;
import org.qiunet.flash.handler.common.player.event.BasePlayerEventData;
import org.qiunet.flash.handler.common.player.event.OfflineUserCreateEvent;
import org.qiunet.flash.handler.common.player.event.OfflineUserDestroyEvent;
import org.qiunet.utils.listener.event.IEventData;

import java.util.concurrent.atomic.AtomicBoolean;

/***
 * 离线玩家actor
 * 仅仅是读取数据使用.
 * 不使用了需要destroy
 *
 * @author qiunet
 * 2021/11/19 11:55
 */
public class OfflinePlayerActor extends MessageHandler<OfflinePlayerActor> implements IPlayerDataLoader  {
	/**
	 * 玩家的数据加载器
	 */
	private final PlayerDataLoader dataLoader;

	OfflinePlayerActor(long playerId) {
		this.dataLoader = new PlayerDataLoader(playerId);
		this.fireEvent(OfflineUserCreateEvent.valueOf(this));
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
	public void fireEvent(IEventData eventData) {
		if (eventData instanceof BasePlayerEventData) {
			((BasePlayerEventData) eventData).setOfflinePlayerActor(this);
		}
		this.addMessage(a -> eventData.fireEventHandler());
	}

	private final AtomicBoolean destroy = new AtomicBoolean();
	public void destroy(){
		if (! destroy.compareAndSet(false, true)) {
			return;
		}
		this.fireEvent(OfflineUserDestroyEvent.valueOf(getPlayerId()));

		UserOfflineManager.instance.remove(getPlayerId());
		this.dataLoader.unregister();
		super.destroy();
	}
}
