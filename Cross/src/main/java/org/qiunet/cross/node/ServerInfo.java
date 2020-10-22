package org.qiunet.cross.node;

import org.qiunet.data.util.DbProperties;
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
	 * 端口
	 */
	private int communicationPort;

	public static ServerInfo valueOf(int communicationPort) {
		return valueOf(DbProperties.getInstance().getServerId(), DbProperties.getInstance().getServerType(), communicationPort);
	}

	public static ServerInfo valueOf(int serverId, ServerType type, int communicationPort) {
		return valueOf(serverId, type, NetUtil.getInnerIp(), communicationPort);
	}

	public static ServerInfo valueOf(int serverId, ServerType type, String host, int communicationPort) {
		ServerInfo node = new ServerInfo();
		node.communicationPort = communicationPort;
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

	public int getPort() {
		return communicationPort;
	}

	public void setPort(int communicationPort) {
		this.communicationPort = communicationPort;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		ServerInfo that = (ServerInfo) o;
		return serverId == that.serverId &&
			communicationPort == that.communicationPort &&
			type == that.type &&
			host.equals(that.host);
	}

	@Override
	public int hashCode() {
		return Objects.hash(serverId, type, host, communicationPort);
	}

	@Override
	public String toString() {
		return "ServerInfo{" +
			"serverId=" + serverId +
			", type=" + type +
			", host='" + host + '\'' +
			", port=" + communicationPort +
			'}';
	}
}
