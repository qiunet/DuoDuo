package org.qiunet.cross.event;

import org.qiunet.cross.actor.CrossPlayerActor;
import org.qiunet.flash.handler.common.player.event.UserEvent;

/***
 * 跨服事件的基础类.
 *
 * @author qiunet
 * 2020-10-15 15:36
 */
public abstract class BaseCrossPlayerEvent extends UserEvent {

	@Override
	public CrossPlayerActor getPlayer() {
		return super.getPlayer();
	}
}
