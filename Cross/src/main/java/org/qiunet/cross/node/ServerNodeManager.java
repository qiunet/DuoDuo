package org.qiunet.cross.node;

/***
 *
 *
 * @author qiunet
 * 2020-10-21 17:21
 */
public class ServerNodeManager {
	/**
	 * 获得一个serverNode
	 * @param serverId
	 * @return
	 */
	public static ServerNode getNode(int serverId) {
		return ServerNodeManager0.instance.getNode(serverId);
	}
}
