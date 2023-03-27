package org.qiunet.cross.node;

import org.qiunet.utils.listener.event.IListenerEvent;

/***
 *
 * @author qiunet
 * 2023/4/1 21:53
 */
public class ServerNodeQuitEvent implements IListenerEvent {
	/**
	 * 要退出的serverId
	 */
	private int serverId;

	public static ServerNodeQuitEvent valueOf(){
		ServerNodeQuitEvent data = new ServerNodeQuitEvent();
	    data.serverId = ServerNodeManager.getCurrServerId();
		return data;
	}

	public int getServerId() {
		return serverId;
	}
}
