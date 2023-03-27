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

	private transient final LazyLoader<ServerType> serverType = new LazyLoader<>(() -> ServerType.getServerType(getServerId()));

	private transient final LazyLoader<Integer> serverGroupId = new LazyLoader<>(() -> ServerType.getGroupId(getServerId()));

	private static final String REDIS_KEY_PREFIX = "SERVER_NODE_INFO_REDIS_KEY#";
	/**
	 * 指定public host 的key
	 */
	private static final String public_host_key = "server.public_host";
	/**
	 * 使用ipv6 需要云服务器配置
	 * 需要重启应用.
	 * 需要打开ip6安全组的对应的端口
	 */
	private static final String use_Ipv6_Key = "server.use_ipv6";
	/**
	 * 最后更新时间
	 */
	private static final String last_update_dt = "lastUpdateDt";
	/**服务端口**/
	private static final String server_port = "serverPort";
	/**公网IP**/
	private static final String public_ip4 = "publicHost";
	/**IP6地址**/
	private static final String public_ip6 = "publicIp6";
	/**服务器ID*/
	private static final String server_id = "serverId";
	/**节点通讯端口**/
	private static final String node_port = "nodePort";
	/**内网IP**/
	private static final String host = "host";

	/** 服务判定离线时间 */
	public static final long SERVER_OFFLINE_SECONDS = 110L;
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
		node.put(host, NetUtil.getInnerIp());
		node.put(ServerInfo.server_port, serverPort);
		node.put(ServerInfo.node_port, nodePort);
		node.put(ServerInfo.server_id, serverId);
		String publicHost = null;
		if (ServerConfig.getConfig() != null) {
			publicHost = ServerConfig.getInstance().getValue(public_host_key);
		}
		// 外网才去自动获取. 本地 测试如果需要外网IP 请配置在server.conf
		if (StringUtil.isEmpty(publicHost) && ServerConfig.isOfficial()) {
			publicHost = NetUtil.getPublicIp4();
			LoggerType.DUODUO_FLASH_HANDLER.error("Use public ip {}", publicHost);
		}

		if (! StringUtil.isEmpty(publicHost)) {
			node.put(ServerInfo.public_ip4, publicHost);
		}

		if (ServerConfig.getConfig() != null && ServerConfig.getConfig().containKey(use_Ipv6_Key)) {
			node.put(ServerInfo.public_ip6, NetUtil.getPublicIp6());
		}

		return node;
	}

	public int getServerId() {
		return (Integer) get(ServerInfo.server_id);
	}

	public ServerType getServerType() {
		return serverType.get();
	}

	public int getServerGroupId(){
		return serverGroupId.get();
	}

	public String getHost() {
		return get(ServerInfo.host).toString();
	}

	public int getNodePort() {
		return (Integer) get(ServerInfo.node_port);
	}

	/**
	 * 服务是否停止了
	 * @return
	 */
	public boolean isOffline(){
		Long dt = (Long) get(ServerInfo.last_update_dt);
		return dt != null && System.currentTimeMillis() - dt > TimeUnit.SECONDS.toMillis(SERVER_OFFLINE_SECONDS);
	}

	public int getServerPort() {
		return (Integer) get(ServerInfo.server_port);
	}

	/**
	 * 得到对外提供的地址
	 * 优先判断有没有对外的地址. 然后再使用内网地址.
	 * @return
	 */
	public String getPublicHost6() {
		return (String) get(ServerInfo.public_ip6);
	}
	/**
	 * 得到对外提供的ip6地址
	 * 优先判断有没有对外的地址. 然后再使用内网地址.
	 * @return
	 */
	public String getPublicHost(){
		if (containsKey(ServerInfo.public_ip4)) {
			return (String) get(ServerInfo.public_ip4);
		}
		return getHost();
	}

	/**
	 * 更新时间刷新
	 */
	public void refreshUpdateDt() {
		this.put(ServerInfo.last_update_dt, System.currentTimeMillis());
	}

	@Override
	public String toString() {
		return JsonUtil.toJsonString(this);
	}
	/**
	 * server info的redis key
	 * @param serverId
	 * @return
	 */
	public static String serverInfoRedisKey(int serverId) {
		return REDIS_KEY_PREFIX+serverId;
	}
}
