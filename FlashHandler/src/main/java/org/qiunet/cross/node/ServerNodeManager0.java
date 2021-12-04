package org.qiunet.cross.node;

import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;
import org.qiunet.cross.common.contants.ScannerParamKey;
import org.qiunet.data.core.support.redis.IRedisUtil;
import org.qiunet.data.core.support.redis.RedisLock;
import org.qiunet.data.util.ServerConfig;
import org.qiunet.data.util.ServerType;
import org.qiunet.flash.handler.netty.client.tcp.NettyTcpClient;
import org.qiunet.flash.handler.netty.server.constants.CloseCause;
import org.qiunet.utils.args.ArgsContainer;
import org.qiunet.utils.args.Argument;
import org.qiunet.utils.async.LazyLoader;
import org.qiunet.utils.exceptions.CustomException;
import org.qiunet.utils.json.JsonUtil;
import org.qiunet.utils.listener.event.EventHandlerWeightType;
import org.qiunet.utils.listener.event.EventListener;
import org.qiunet.utils.listener.event.data.ServerShutdownEventData;
import org.qiunet.utils.listener.event.data.ServerStartupEventData;
import org.qiunet.utils.logger.LoggerType;
import org.qiunet.utils.math.MathUtil;
import org.qiunet.utils.scanner.IApplicationContext;
import org.qiunet.utils.scanner.IApplicationContextAware;
import org.qiunet.utils.scanner.ScannerType;
import org.qiunet.utils.string.StringUtil;
import org.qiunet.utils.timer.TimerManager;
import org.slf4j.Logger;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;
import java.util.stream.Collectors;

/***
 * 管理server节点.
 * 负责节点信息上报.
 * 如果没有开跨服. 就不会启动该类.
 *
 * @author qiunet
 * 2020-10-09 11:21
 */
enum ServerNodeManager0 implements IApplicationContextAware {
	instance;
	// server node 创建同步锁redis key
	private static final String SERVER_NODE_CREATE_SYNC_LOCK_KEY = "server_node_create_sync_lock_key_";
	// 所有当前的节点
	private static final Map<Integer, ServerNode> nodes = Maps.newConcurrentMap();
	private final Logger logger = LoggerType.DUODUO_CROSS.getLogger();
	// 服务判定离线时间
	public static final long SERVER_OFFLINE_SECONDS = 110;
	// redis
	private IRedisUtil redisUtil;
	/**
	 * 服务器的信息. 支持增加自定义字段.
	 */
	private ServerInfo currServerInfo;
	/**
	 * 添加一个服务器节点
	 * @param node
	 */
	synchronized void addNode(ServerNode node) {
		Preconditions.checkState(node.isAuth(), "ServerNode need auth");
		ServerNode serverNode = nodes.get(node.getServerId());

		if (serverNode != null) {
			serverNode.getSender().close(CloseCause.INACTIVE);
		}

		node.getSender().addCloseListener((session, cause) -> nodes.remove(node.getServerId()));
		nodes.put(node.getServerId(), node);
	}

	/**
	 * 获得serverInfo
	 * @param serverId
	 * @return
	 */
	ServerInfo getServerInfo(int serverId) {
		int groupId = ServerType.getGroupId(serverId);
		ServerType serverType = ServerType.getServerType(serverId);
		String serverInfoStr = redisUtil.returnJedis().hget(serverNodeRedisKey(serverType, groupId), String.valueOf(serverId));
		if (StringUtil.isEmpty(serverInfoStr)) {
			throw new CustomException("ServerId [{}] is not online!", serverId);
		}

		return JsonUtil.getGeneralObject(serverInfoStr, ServerInfo.class);
	}

	/**
	 * 获得serverNode
	 * @param serverId
	 * @return
	 */
	ServerNode getNode(int serverId) {
		if (serverId == currServerInfo.getServerId()) {
			throw new CustomException("It is current server!!");
		}

		ServerNode serverNode = nodes.get(serverId);
		if (serverNode != null) {
			return serverNode;
		}
		return lockAndCreateServerNode(serverId);
	}

	/**
	 * 锁定. 然后创建serverNode
	 * @param serverId
	 */
	private synchronized ServerNode lockAndCreateServerNode(int serverId) {
		RedisLock redisLock = redisUtil.redisLock(this.createRedisKey(serverId));
		try {
			if (redisLock.lock()) {
				if (nodes.containsKey(serverId)) {
					return nodes.get(serverId);
				}
				return new ServerNode(redisLock, this.getServerInfo(serverId));
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		throw new CustomException("Create server node [{}] fail", serverId);
	}

	@Override
	public int order() {
		return 10;
	}

	@Override
	public void setApplicationContext(IApplicationContext context, ArgsContainer argsContainer) throws Exception {

		this.currServerInfo = argsContainer.isNull(ScannerParamKey.CUSTOM_SERVER_INFO)
			? ServerInfo.valueOf(ServerConfig.getServerPort(), ServerConfig.getNodePort())
			: argsContainer.getArgument(ScannerParamKey.CUSTOM_SERVER_INFO).get();

		Argument<Supplier<IRedisUtil>> redisArg = argsContainer.getArgument(ScannerParamKey.SERVER_NODE_REDIS_INSTANCE_SUPPLIER);
		if (! redisArg.isNull()) {
			this.redisUtil = redisArg.get().get();

			// 启动检测 redis 是否通畅.
			this.redisUtil.returnJedis().exists("");
		}
	}

	@EventListener
	private void onServerStart(ServerStartupEventData data){
		if (ServerConfig.getNodePort() == 0) {
			return;
		}

		TimerManager.executor.scheduleAtFixedRate(this::refreshServerInfo, MathUtil.random(0, 200), TimeUnit.SECONDS.toMillis(60), TimeUnit.MILLISECONDS);
	}

	ServerInfo getCurrServerInfo() {
		return currServerInfo;
	}

	/**
	 * 获得指定type的redis key
	 * @param serverType
	 * @return
	 */
	private String serverNodeRedisKey(ServerType serverType, int groupId) {
		return "SERVER_NODE_REDIS_MAP_KEY#"+serverType + "#" + groupId;
	}

	private final LazyLoader<String> REDIS_SERVER_NODE_INFO_KEY = new LazyLoader<>(() -> {
		int groupId = ServerType.getGroupId(ServerConfig.getServerId());
		return serverNodeRedisKey(currServerInfo.getServerType(), groupId);
	});
	/**
	 * 每一分钟, 刷新server info
	 */
	private void refreshServerInfo() {
		currServerInfo.put(ServerInfo.lastUpdateDt, System.currentTimeMillis());
		redisUtil.returnJedis(false).hset(REDIS_SERVER_NODE_INFO_KEY.get(), String.valueOf(currServerInfo.getServerId()), currServerInfo.toString());
		// 触发心跳.
		ServerNodeTickEvent.instance.fireEventHandler();
	}

	@Override
	public ScannerType scannerType() {
		return ScannerType.SERVER;
	}

	/**
	 * 获得指定类型的部分id的serverInfo
	 * @param serverType
	 * @param groupId
	 * @return
	 */
	List<ServerInfo> getServerInfos(ServerType serverType, int groupId) {
		if (serverType == null) {
			throw new NullPointerException();
		}

		String redisKey = serverNodeRedisKey(serverType, groupId);
		List<String> stringList = redisUtil.returnJedis().hvals(redisKey);

		return stringList.stream()
				.map(json -> JsonUtil.getGeneralObject(json, ServerInfo.class))
				.collect(Collectors.toList());
	}

	/**
	 * 获得该类型 该组的服务器数量
	 * @param serverType
	 * @param groupId
	 * @return
	 */
	long getServerCount(ServerType serverType, int groupId) {
		if (serverType == null) {
			throw new NullPointerException();
		}

		String redisKey = serverNodeRedisKey(serverType, groupId);
		Long val =  redisUtil.returnJedis().hlen(redisKey);
		return val == null ? 0: val;
	}

	@EventListener(EventHandlerWeightType.HIGHEST)
	private void onShutdown(ServerShutdownEventData data) {
		if (redisUtil == null) {
			return;
		}

		redisUtil.execCommands(jedis -> {
			jedis.hdel(REDIS_SERVER_NODE_INFO_KEY.get(), String.valueOf(currServerInfo.getServerId()));
			return null;
		});
		nodes.values().forEach(node -> node.getSender().close(CloseCause.SERVER_SHUTDOWN));
		NettyTcpClient.shutdown();
	}

	/**
	 * 获得创建使用的key
	 * @param serverId
	 * @return
	 */
	private String createRedisKey(int serverId) {
		if (serverId < getCurrServerInfo().getServerId()) {
			return SERVER_NODE_CREATE_SYNC_LOCK_KEY + serverId +"_"+ getCurrServerInfo().getServerId();
		}else {
			return SERVER_NODE_CREATE_SYNC_LOCK_KEY + getCurrServerInfo().getServerId() +"_"+  serverId;
		}
	}
}
