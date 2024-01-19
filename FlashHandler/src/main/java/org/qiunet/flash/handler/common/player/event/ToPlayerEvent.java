package org.qiunet.flash.handler.common.player.event;

import org.qiunet.utils.listener.event.ICrossListenerEvent;

/***
 * 带玩家参数的事件数据.
 * 只能是在线玩家触发. 如果要触发非在线玩家.
 * Cross to Game player的跨服事件
 *
 * @author qiunet
 * 2020-10-13 20:27
 */
public abstract class ToPlayerEvent extends PlayerEvent implements ICrossListenerEvent {

}
