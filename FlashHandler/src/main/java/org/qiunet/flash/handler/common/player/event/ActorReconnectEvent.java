package org.qiunet.flash.handler.common.player.event;


/***
 * 玩家重连处理
 *
 * @author qiunet
 * 2021/11/26 17:42
 */
public class ActorReconnectEvent extends PlayerEvent {

	public static ActorReconnectEvent valueOf(){
		return new ActorReconnectEvent();
	}
}
