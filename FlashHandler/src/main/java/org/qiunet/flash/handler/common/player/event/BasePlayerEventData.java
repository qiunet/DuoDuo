package org.qiunet.flash.handler.common.player.event;

import org.qiunet.flash.handler.common.player.PlayerActor;

/***
 * 带玩家参数的事件数据.
 * 只能是在线玩家触发. 如果要触发非在线玩家.
 * 请使用{@link UserEventData}
 *
 * @author qiunet
 * 2020-10-13 20:27
 */
public abstract class BasePlayerEventData extends UserEventData {

	@Override
	public PlayerActor getPlayer() {
		return super.getPlayer();
	}
}
