package org.qiunet.cross.node;

import org.qiunet.data.util.ServerConfig;
import org.qiunet.data.util.ServerType;
import org.qiunet.utils.async.LazyLoader;
import org.qiunet.utils.json.JsonUtil;
import org.qiunet.utils.logger.LoggerType;
import org.qiunet.utils.net.NetUtil;
import org.qiunet.utils.string.StringUtil;

import java.util.HashMap;
import java.util.concurrent.TimeUnit;

/***
 * 服务节点
 *
 * @author qiunet
 * 2020-10-09 11:07
 */
public final class ServerInfo extends HashMap<String, Object> {
	static final String publicHostKey = "server.public_host";
	/**
	 * 使用ipv6 需要云服务器配置
	 * 需要重启应用.
	 * 需要打开ip6安全组的对应的端口
	 */
	static final String useIpv6Key = "server.use_ipv6";
	static final String lastUpdateDt = "lastUpdateDt";

	private transient final LazyLoader<ServerType> serverType = new LazyLoader<>(() -> ServerType.getServerType(getServerId()));

	private transient final LazyLoader<Integer> serverGroupId = new LazyLoader<>(() -> ServerType.getGroupId(getServerId()));
	/**
	 *
	 * @param serverPort 对外服务端口
	 * @param nodePort 服务间交互端口
	 * @return
	 */
	public static ServerInfo valueOf(int serverPort, int nodePort) {
		return valueOf(ServerConfig.getServerId(), serverPort, nodePort);
	}

	/**
	 *
	 * @param serverId 分配的服务id
	 * @param serverPort 对外服务端口
	 * @param nodePort 服务间交互端口
	 * @return
	 */
	public static ServerInfo valueOf(int serverId, int serverPort, int nodePort) {
		ServerInfo node = new ServerInfo();
		node.put("host", NetUtil.getInnerIp());
		node.put("serverPort", serverPort);
		node.put("nodePort", nodePort);
		node.put("serverId", serverId);
		if (ServerConfig.getConfig() != null && ServerConfig.getConfig().containKey(ServerConfig.CROSS_PORT)) {
			node.put("crossPort", ServerConfig.getConfig().getInt(ServerConfig.CROSS_PORT));
		}
		String publicHost = null;
		if (ServerConfig.getConfig() != null) {
			publicHost = ServerConfig.getInstance().getValue(publicHostKey);
		}
		// 外网才去自动获取. 本地 测试如果需要外网IP 请配置在server.conf
		if (StringUtil.isEmpty(publicHost) && ServerConfig.isOfficial()) {
			publicHost = NetUtil.getPublicIp4();
			LoggerType.DUODUO_FLASH_HANDLER.error("Use public ip {}", publicHost);
		}

		if (! StringUtil.isEmpty(publicHost)) {
			node.put("publicHost", publicHost);
		}

		if (ServerConfig.getConfig() != null && ServerConfig.getConfig().containKey(useIpv6Key)) {
			node.put("publicIp6", NetUtil.getPublicIp6());
		}

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

	public int getNodePort() {
		return (Integer) get("nodePort");
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

	/**
	 * 得到对外提供的地址
	 * 优先判断有没有对外的地址. 然后再使用内网地址.
	 * @return
	 */
	public String getPublicHost6() {
		return (String) get("publicIp6");
	}
	/**
	 * 得到对外提供的ip6地址
	 * 优先判断有没有对外的地址. 然后再使用内网地址.
	 * @return
	 */
	public String getPublicHost(){
		if (containsKey("publicHost")) {
			return (String) get("publicHost");
		}
		return getHost();
	}


	@Override
	public String toString() {
		return JsonUtil.toJsonString(this);
	}

	/**
	 * 玩法服叫 server_port
	 * 游戏服叫 cross_port
	 * @return
	 */
	public int getCrossPort() {
		if (getServerType() == ServerType.CROSS) {
			return getServerPort();
		}
		return (Integer) get("crossPort");
	}
}
