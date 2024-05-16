package org.qiunet.cross.node;

import org.qiunet.data.conf.ServerConfig;
import org.qiunet.data.enums.ServerType;
import org.qiunet.utils.async.LazyLoader;
import org.qiunet.utils.json.JsonUtil;
import org.qiunet.utils.logger.LoggerType;
import org.qiunet.utils.math.IWeightObj;
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
public final class ServerInfo extends HashMap<String, Object> implements IWeightObj {

	private transient final LazyLoader<ServerType> serverType = new LazyLoader<>(() -> ServerType.getServerType(getServerId()));

	private static final String REDIS_KEY_PREFIX = "SERVER_NODE_INFO_REDIS_KEY#";
	/**
	 * 指定public host 的key
	 */
	private static final String PUBLIC_HOST_KEY = "server.public_host";
	/**
	 * 使用ipv6 需要云服务器配置
	 * 需要重启应用.
	 * 需要打开ip6安全组的对应的端口
	 */
	private static final String USE_IPV_6_KEY = "server.use_ipv6";
	/**
	 * 最后更新时间
	 */
	private static final String LAST_UPDATE_DT = "lastUpdateDt";
	/**服务端口**/
	private static final String SERVER_PORT = "serverPort";
	/**公网IP**/
	private static final String PUBLIC_IP_4 = "publicHost";
	/**IP6地址**/
	private static final String PUBLIC_IP_6 = "publicIp6";
	/**服务器ID*/
	private static final String SERVER_ID = "serverId";
	/**节点通讯端口**/
	private static final String NODE_PORT = "nodePort";
	/**内网IP**/
	private static final String WEIGHT = "weight";
	/**内网IP**/
	private static final String HOST = "host";

	/** 服务判定离线时间 */
	public static final long SERVER_OFFLINE_SECONDS = 110L;
	/**
	 *
	 * @param nodePort 服务间交互端口
	 * @return
	 */
	public static ServerInfo selfInfo(int serverPort, int nodePort) {
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
		node.put(ServerInfo.WEIGHT, Integer.MAX_VALUE);
		node.put(ServerInfo.SERVER_PORT, serverPort);
		node.put(ServerInfo.NODE_PORT, nodePort);
		node.put(ServerInfo.SERVER_ID, serverId);
		node.put(HOST, NetUtil.getInnerIp());
		String publicHost = null;
		if (ServerConfig.getConfig() != null) {
			publicHost = ServerConfig.getInstance().getValue(PUBLIC_HOST_KEY);
		}
		// 外网才去自动获取. 本地 测试如果需要外网IP 请配置在server.conf
		if (StringUtil.isEmpty(publicHost) && ServerConfig.isOfficial()) {
			publicHost = NetUtil.getPublicIp4();
			LoggerType.DUODUO_FLASH_HANDLER.error("Use public ip {}", publicHost);
		}

		if (! StringUtil.isEmpty(publicHost)) {
			node.put(ServerInfo.PUBLIC_IP_4, publicHost);
		}

		if (ServerConfig.getConfig() != null && ServerConfig.getConfig().getBoolean(USE_IPV_6_KEY)) {
			node.put(ServerInfo.PUBLIC_IP_6, NetUtil.getPublicIp6());
		}

		return node;
	}


	public void setWeight(int weightVal) {
		put(WEIGHT, weightVal);
	}

	public int getServerId() {
		return (Integer) get(ServerInfo.SERVER_ID);
	}

	public ServerType getServerType() {
		return serverType.get();
	}

	public String getHost() {
		return get(ServerInfo.HOST).toString();
	}

	public int getNodePort() {
		return (Integer) get(ServerInfo.NODE_PORT);
	}

	/**
	 * 服务是否停止了
	 * @return
	 */
	public boolean isOffline(){
		Long dt = (Long) get(ServerInfo.LAST_UPDATE_DT);
		return dt != null && System.currentTimeMillis() - dt > TimeUnit.SECONDS.toMillis(SERVER_OFFLINE_SECONDS);
	}

	public int getServerPort() {
		return (Integer) get(ServerInfo.SERVER_PORT);
	}

	/**
	 * 得到对外提供的地址
	 * 优先判断有没有对外的地址. 然后再使用内网地址.
	 * @return
	 */
	public String getPublicHost6() {
		return (String) get(ServerInfo.PUBLIC_IP_6);
	}
	/**
	 * 得到对外提供的ip6地址
	 * 优先判断有没有对外的地址. 然后再使用内网地址.
	 * @return
	 */
	public String getPublicHost(){
		if (containsKey(ServerInfo.PUBLIC_IP_4)) {
			return (String) get(ServerInfo.PUBLIC_IP_4);
		}
		return getHost();
	}

	/**
	 * 更新时间刷新
	 */
	public void refreshUpdateDt() {
		this.put(ServerInfo.LAST_UPDATE_DT, System.currentTimeMillis());
	}

	@Override
	public String toString() {
		return JsonUtil.toJsonString(this);
	}
	/**
	 * server info的redis key
	 * @param serverId server id
	 * @return server info redis key
	 */
	public static String serverInfoRedisKey(String serverId) {
		return REDIS_KEY_PREFIX+serverId;
	}
	/**
	 * server info的redis key
	 * @param serverId server id
	 * @return server info redis key
	 */
	public static String serverInfoRedisKey(int serverId) {
		return serverInfoRedisKey(String.valueOf(serverId));
	}

	@Override
	public int weight() {
		return (Integer) get(WEIGHT);
	}
}
