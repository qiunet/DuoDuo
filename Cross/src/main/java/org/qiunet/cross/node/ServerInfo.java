package org.qiunet.cross.node;

import org.qiunet.data.util.ServerConfig;
import org.qiunet.data.util.ServerType;
import org.qiunet.utils.net.NetUtil;

import java.util.Objects;

/***
 * 服务节点
 *
 * @author qiunet
 * 2020-10-09 11:07
 */
public class ServerInfo {
	/**
	 * 分配的服务id
	 */
	private int serverId;
	/**
	 * 类型
	 */
	private ServerType type;
	/**
	 * 内网地址
	 */
	private String host;
	/**
	 * 服务间交互端口
	 */
	private int communicationPort;
	/**
	 * 对外服务端口
	 */
	private int serverPort;

	public static ServerInfo valueOf(int serverPort, int communicationPort) {
		return valueOf(ServerConfig.getServerId(), ServerConfig.getServerType(), serverPort, communicationPort);
	}

	public static ServerInfo valueOf(int serverId, ServerType type, int serverPort, int communicationPort) {
		return valueOf(serverId, type, NetUtil.getInnerIp(), serverPort, communicationPort);
	}

	public static ServerInfo valueOf(int serverId, ServerType type, String host, int serverPort, int communicationPort) {
		ServerInfo node = new ServerInfo();
		node.communicationPort = communicationPort;
		node.serverPort = serverPort;
		node.serverId = serverId;
		node.type = type;
		node.host = host;
		return node;
	}

	public int getServerId() {
		return serverId;
	}

	public void setServerId(int serverId) {
		this.serverId = serverId;
	}

	public ServerType getType() {
		return type;
	}

	public void setType(ServerType type) {
		this.type = type;
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public int getCommunicationPort() {
		return communicationPort;
	}

	public void setCommunicationPort(int communicationPort) {
		this.communicationPort = communicationPort;
	}

	public int getServerPort() {
		return serverPort;
	}

	public void setServerPort(int serverPort) {
		this.serverPort = serverPort;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		ServerInfo that = (ServerInfo) o;
		return serverId == that.serverId &&
			serverPort == that.serverPort &&
			communicationPort == that.communicationPort &&
			type == that.type &&
			host.equals(that.host);
	}

	@Override
	public int hashCode() {
		return Objects.hash(serverId, type, host, serverPort, communicationPort);
	}

	@Override
	public String toString() {
		return "ServerInfo{" +
			"serverId=" + serverId +
			", type=" + type +
			", host='" + host + '\'' +
			", serverPort=" + serverPort +
			", communicationPort=" + communicationPort +
			'}';
	}
}
