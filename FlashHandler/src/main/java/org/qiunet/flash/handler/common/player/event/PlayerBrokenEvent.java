package org.qiunet.flash.handler.common.player.event;


import org.qiunet.cross.event.ToCrossPlayerEvent;

/***
 * 玩家非登出下线处理
 * cross的地方需要处理.
 *
 * @author qiunet
 * 2021/11/26 17:42
 */
public class PlayerBrokenEvent extends ToCrossPlayerEvent {

	public static PlayerBrokenEvent valueOf(){
		return new PlayerBrokenEvent();
	}
}
