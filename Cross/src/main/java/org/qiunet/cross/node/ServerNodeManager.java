package org.qiunet.cross.node;

import org.qiunet.data.util.ServerType;

/***
 *
 *
 * @author qiunet
 * 2020-10-21 17:21
 */
public class ServerNodeManager {

	public static int getCurrServerId(){
		return ServerNodeManager0.instance.getCurrServerInfo().getServerId();
	}

	public static ServerType getCurrServerType(){
		return ServerNodeManager0.instance.getCurrServerInfo().getType();
	}
	/**
	 * 获得一个serverNode
	 * @param serverId
	 * @return
	 */
	public static ServerNode getNode(int serverId) {
		return ServerNodeManager0.instance.getNode(serverId);
	}
}
