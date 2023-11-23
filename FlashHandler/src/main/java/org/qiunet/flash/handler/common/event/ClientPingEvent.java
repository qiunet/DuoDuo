package org.qiunet.flash.handler.common.event;

import org.qiunet.flash.handler.common.enums.ServerConnType;
import org.qiunet.flash.handler.common.player.event.BasePlayerEvent;

/***
 * 玩家ping事件
 *
 * @author qiunet
 * 2022/11/1 10:21
 */
public class ClientPingEvent extends BasePlayerEvent {

	private ServerConnType type;

	public static ClientPingEvent valueOf(ServerConnType type){
		ClientPingEvent data = new ClientPingEvent();
	    data.type = type;
		return data;
	}

	public ServerConnType getType() {
		return type;
	}
}

