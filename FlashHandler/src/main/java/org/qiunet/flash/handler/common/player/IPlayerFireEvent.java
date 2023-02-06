package org.qiunet.flash.handler.common.player;

import org.qiunet.flash.handler.common.IMessageHandler;
import org.qiunet.flash.handler.common.player.event.UserEvent;
import org.qiunet.utils.listener.event.EventManager;

/***
 * 玩家事件触发
 *
 * @author qiunet
 * 2023/2/7 15:03
 */
public interface IPlayerFireEvent<E extends UserEvent, C extends UserEvent, P extends AbstractUserActor<P>> extends IMessageHandler<P> {
	/**
	 * 触发事件
	 * @param event 事件数据
	 */
	default void fireEvent(E event) {
		EventManager.fireEventHandler(event.setPlayer((IPlayer) this));
	}
	/**
	 * 异步触发事件
	 * @param event 事件数据
	 */
	default void fireAsyncEvent(E event) {
		this.addMessage(p -> this.fireEvent(event));
	}
	/**
	 * 跨服事件
	 * @param event 事件数据
	 */
	void fireCrossEvent(C event);
}
