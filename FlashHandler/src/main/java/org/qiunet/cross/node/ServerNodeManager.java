package org.qiunet.cross.node;

import org.qiunet.data.util.ServerType;

import java.util.List;

/***
 *
 *
 * @author qiunet
 * 2020-10-21 17:21
 */
public class ServerNodeManager {
	/**
	 * 获得当前的serverId
	 * @return
	 */
	public static int getCurrServerId(){
		return getCurrServerInfo().getServerId();
	}

	/**
	 * 得到当前的ServerInfo
	 * @return
	 */
	public static ServerInfo getCurrServerInfo() {
		return ServerNodeManager0.instance.getCurrServerInfo();
	}
	/**
	 * 获得当前的serverType
	 * @return
	 */
	public static ServerType getCurrServerType(){
		return getCurrServerInfo().getServerType();
	}

	/**
	 * 是否是废弃服务器
	 * @return
	 */
	public static boolean isDeprecatedServer(){
		return ServerNodeManager0.instance.deprecated.get();
	}

	/**
	 * 是否对外停止服务.
	 * @return
	 */
	public static boolean isServerClosed(){
		return ServerNodeManager0.instance.serverClosed.get();
	}
	/**
	 * 获得serverInfo
	 * @param serverId
	 * @return
	 */
	public static ServerInfo getServerInfo(int serverId) {
		return ServerNodeManager0.instance.getServerInfo(serverId);
	}
	/**
	 * 获得 某个server 类型的所有的 server info
	 * @param type 类型
	 * @return 列表
	 */
	public static List<ServerInfo> getServerInfoList(ServerType type) {
		return ServerNodeManager0.instance.getServerInfoList(type);
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
