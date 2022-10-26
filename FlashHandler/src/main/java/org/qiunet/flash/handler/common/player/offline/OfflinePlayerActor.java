package org.qiunet.flash.handler.common.player.offline;

import com.google.common.base.Preconditions;
import org.qiunet.data.async.ISyncDbMessage;
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
public class OfflinePlayerActor extends MessageHandler<OfflinePlayerActor> implements IPlayerDataLoader, IPlayer, ISyncDbMessage {
	/**
	 * 玩家的数据加载器
	 */
	private final PlayerDataLoader dataLoader;
	private String msgExecuteIndex;

	OfflinePlayerActor(long playerId) {
		this.fireEvent(OfflineUserCreateEvent.valueOf(this));
		this.dataLoader = new PlayerDataLoader(this, this, playerId);
	}

	public void setMsgExecuteIndex(String msgExecuteIndex) {
		this.msgExecuteIndex = msgExecuteIndex;
	}

	@Override
	public String msgExecuteIndex() {
		Preconditions.checkNotNull(msgExecuteIndex, "Need set msgExecuteIndex in OfflineUserCreateEvent!");
		return msgExecuteIndex;
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

	@Override
	public void syncBbMessage(Runnable runnable) {
		this.addMessage(h -> runnable.run());
	}
}
