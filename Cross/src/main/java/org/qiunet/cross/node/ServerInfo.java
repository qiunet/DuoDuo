package org.qiunet.cross.node;

import org.qiunet.data.util.ServerType;

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
	 * 端口
	 */
	private int port;

	public static ServerInfo valueOf(int serverId, ServerType type, String host, int port) {
		ServerInfo node = new ServerInfo();
		node.serverId = serverId;
		node.type = type;
		node.host = host;
		node.port = port;
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

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		ServerInfo that = (ServerInfo) o;
		return serverId == that.serverId &&
			port == that.port &&
			type == that.type &&
			host.equals(that.host);
	}

	@Override
	public int hashCode() {
		return Objects.hash(serverId, type, host, port);
	}

	@Override
	public String toString() {
		return "ServerInfo{" +
			"serverId=" + serverId +
			", type=" + type +
			", host='" + host + '\'' +
			", port=" + port +
			'}';
	}
}
