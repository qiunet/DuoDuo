package org.qiunet.flash.handler.common.player.event;

import com.baidu.bjf.remoting.protobuf.annotation.Ignore;
import org.qiunet.flash.handler.common.player.AbstractUserActor;
import org.qiunet.utils.listener.event.IListenerEvent;

/***
 * 玩家事件的共同父类
 * 因为 UserEventData 细分后. 跨服来回触发事件容易有问题. 统一一个.
 * 需要业务自己根据情况. 判断player是什么.
 * @author qiunet
 * 2020-10-21 11:02
 */
public abstract class UserEvent implements IListenerEvent {
	/**
	 * 玩家的对象.
	 */
	@Ignore
	private AbstractUserActor player;

	public <T extends AbstractUserActor> T getPlayer() {
		return (T) player;
	}

	public UserEvent setPlayer(AbstractUserActor player) {
		this.player = player;
		return this;
	}
}
