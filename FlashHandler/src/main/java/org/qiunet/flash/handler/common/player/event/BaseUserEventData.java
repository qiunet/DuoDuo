package org.qiunet.flash.handler.common.player.event;

import com.baidu.bjf.remoting.protobuf.annotation.Ignore;
import org.qiunet.flash.handler.common.player.AbstractUserActor;
import org.qiunet.listener.event.IEventData;

/***
 * 玩家事件的共同父类
 *
 * @author qiunet
 * 2020-10-21 11:02
 */
public class BaseUserEventData<T extends AbstractUserActor> implements IEventData {
	/**
	 * 玩家的对象.
	 */
	@Ignore
	private T player;

	public T getPlayer() {
		return player;
	}

	public void setPlayer(T player) {
		this.player = player;
	}
}
