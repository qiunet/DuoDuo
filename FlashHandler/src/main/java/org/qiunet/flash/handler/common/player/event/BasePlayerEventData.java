package org.qiunet.flash.handler.common.player.event;

import org.qiunet.flash.handler.common.player.AbstractUserActor;
import org.qiunet.listener.event.IEventData;

/***
 * 带玩家参数的事件数据.
 *
 * @author qiunet
 * 2020-10-13 20:27
 */
public abstract class BasePlayerEventData<T extends AbstractUserActor<T>> implements IEventData {
	/**
	 * 玩家的对象.
	 */
	private T player;

	public T getPlayer() {
		return player;
	}

	public void setPlayer(T player) {
		this.player = player;
	}
}
