package org.qiunet.flash.handler.common.player.event;

/***
 * 玩家被销毁事件
 * @author qiunet
 * 2023/6/16 11:08
 */
public class PlayerDestroyEvent extends UserEvent {

	public static PlayerDestroyEvent valueOf(){
		return new PlayerDestroyEvent();
	}
}
