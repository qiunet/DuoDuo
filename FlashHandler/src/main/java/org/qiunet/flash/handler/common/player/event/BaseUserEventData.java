package org.qiunet.flash.handler.common.player.event;

import com.baidu.bjf.remoting.protobuf.annotation.Ignore;
import org.qiunet.flash.handler.common.player.AbstractUserActor;
import org.qiunet.utils.listener.event.IEventData;

/***
 * 玩家事件的共同父类
 *
 * @author qiunet
 * 2020-10-21 11:02
 */
public abstract class BaseUserEventData<T extends AbstractUserActor<T>> implements IEventData {
	/**
	 * 玩家的对象.
	 */
	@Ignore
	private T player;
	/**
	 * 玩家id
	 * 不在线的玩家触发的事件. player为null, playerId有.
	 */
	@Ignore
	private long playerId;

	public T getPlayer() {
		return player;
	}

	public long getPlayerId() {
		return playerId;
	}

	public void setPlayerId(long playerId) {
		this.playerId = playerId;
	}

	public void setPlayer(T player) {
		this.playerId = player.getId();
		this.player = player;
	}
}
