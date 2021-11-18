package org.qiunet.cross.node;

import org.qiunet.data.util.ServerConfig;
import org.qiunet.data.util.ServerType;
import org.qiunet.utils.async.LazyLoader;
import org.qiunet.utils.json.JsonUtil;
import org.qiunet.utils.net.NetUtil;

import java.util.HashMap;
import java.util.concurrent.TimeUnit;

/***
 * 服务节点
 *
 * @author qiunet
 * 2020-10-09 11:07
 */
public final class ServerInfo extends HashMap<String, Object> {
	static final String lastUpdateDt = "lastUpdateDt";

	private transient final LazyLoader<ServerType> serverType = new LazyLoader<>(() -> ServerType.getServerType(getServerId()));

	private transient final LazyLoader<Integer> serverGroupId = new LazyLoader<>(() -> ServerType.getGroupId(getServerId()));
	/**
	 *
	 * @param serverPort 对外服务端口
	 * @param communicationPort 服务间交互端口
	 * @return
	 */
	public static ServerInfo valueOf(int serverPort, int communicationPort) {
		return valueOf(ServerConfig.getServerId(), serverPort, communicationPort);
	}

	/**
	 *
	 * @param serverId 分配的服务id
	 * @param serverPort 对外服务端口
	 * @param communicationPort 服务间交互端口
	 * @return
	 */
	public static ServerInfo valueOf(int serverId, int serverPort, int communicationPort) {
		return valueOf(serverId, NetUtil.getInnerIp(), serverPort, communicationPort);
	}

	/**
	 *
	 * @param serverId 分配的服务id
	 * @param host 内网地址
	 * @param serverPort 对外服务端口
	 * @param communicationPort 服务间交互端口
	 * @return
	 */
	public static ServerInfo valueOf(int serverId, String host, int serverPort, int communicationPort) {
		ServerInfo node = new ServerInfo();
		node.put("communicationPort", communicationPort);
		node.put("serverPort", serverPort);
		node.put("serverId", serverId);
		node.put("host", host);
		return node;
	}

	public int getServerId() {
		return (Integer) get("serverId");
	}

	public ServerType getServerType() {
		return serverType.get();
	}

	public int getServerGroupId(){
		return serverGroupId.get();
	}

	public String getHost() {
		return get("host").toString();
	}

	public int getCommunicationPort() {
		return (Integer) get("communicationPort");
	}

	/**
	 * 服务是否停止了
	 * @return
	 */
	public boolean isOffline(){
		Long dt = (Long) get(lastUpdateDt);
		return dt != null && System.currentTimeMillis() - dt > TimeUnit.SECONDS.toMillis(ServerNodeManager0.SERVER_OFFLINE_SECONDS);
	}

	public int getServerPort() {
		return (Integer) get("serverPort");
	}

	@Override
	public String toString() {
		return JsonUtil.toJsonString(this);
	}

}
