package org.qiunet.flash.handler.common.player;

import org.qiunet.flash.handler.common.IMessageHandler;
import org.qiunet.flash.handler.common.player.event.UserEvent;
import org.qiunet.utils.listener.event.EventManager;
import org.qiunet.utils.listener.event.ICrossListenerEvent;

/***
 * 玩家事件触发
 *
 * @author qiunet
 * 2023/2/7 15:03
 */
public interface IPlayerFireEvent<E extends UserEvent, C extends UserEvent & ICrossListenerEvent, P extends AbstractUserActor<P>> extends IMessageHandler<P> {
	/**
	 * 触发事件 必须是自己的线程
	 * @param event 事件数据
	 */
	default void fireEvent(E event) {
		if (! inSelfThread()) {
			this.fireAsyncEvent(event);
		}else {
			EventManager.fireEventHandler(event.setPlayer((AbstractUserActor) this));
		}
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
