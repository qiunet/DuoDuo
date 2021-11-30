package org.qiunet.test.cross.common.event;

import org.qiunet.utils.listener.event.IEventData;

/***
 * serverNode event
 * @author qiunet
 * 2021/11/30 18:52
 */
public class CrossNodeEvent implements IEventData {
	private long playerId;

	public static CrossNodeEvent valueOf(long playerId){
		CrossNodeEvent data = new CrossNodeEvent();
		data.playerId = playerId;
		return data;
	}

	public long getPlayerId() {
		return playerId;
	}
}
