package org.qiunet.flash.handler.common.event;

import org.qiunet.flash.handler.common.player.event.BasePlayerEventData;

/***
 * 玩家ping事件
 *
 * @author qiunet
 * 2022/11/1 10:21
 */
public class ClientPingEvent extends BasePlayerEventData {
	private static final ClientPingEvent instance = new ClientPingEvent();

	public static ClientPingEvent getInstance() {
		return instance;
	}
}
