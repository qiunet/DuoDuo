package org.qiunet.flash.handler.common.player.event;

import org.qiunet.cross.event.ToCrossPlayerEvent;

/***
 * 玩家重连处理
 * 监听需要判断是CrossPlayerActor 还是 PlayerActor
 *
 * @author qiunet
 * 2021/11/26 17:42
 */
public class PlayerReconnectEvent extends ToCrossPlayerEvent {

	public static PlayerReconnectEvent valueOf(){
		return new PlayerReconnectEvent();
	}
}
