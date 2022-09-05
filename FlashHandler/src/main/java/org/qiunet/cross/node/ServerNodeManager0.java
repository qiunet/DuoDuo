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
import org.qiunet.utils.exceptions.CustomException;
import org.qiunet.utils.json.JsonUtil;
import org.qiunet.utils.listener.event.EventHandlerWeightType;
import org.qiunet.utils.listener.event.EventListener;
import org.qiunet.utils.listener.event.data.ServerClosedEvent;
import org.qiunet.utils.listener.event.data.ServerDeprecatedEvent;
import org.qiunet.utils.listener.event.data.ServerShutdownEventData;
import org.qiunet.utils.listener.event.data.ServerStartupEventData;
import org.qiunet.utils.logger.LoggerType;
import org.qiunet.utils.math.MathUtil;
import org.qiunet.utils.scanner.IApplicationContext;
import org.qiunet.utils.scanner.IApplicationContextAware;
import org.qiunet.utils.scanner.ScannerType;
import org.qiunet.utils.string.StringUtil;
import org.qiunet.utils.timer.TimerManager;
import redis.clients.jedis.params.SetParams;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Supplier;

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
	private final Map<Integer, ServerNode> nodes = Maps.newConcurrentMap();
	/** 服务器已经过期. 不再上传信息 . login 不再分配进入.*/
	final AtomicBoolean deprecated = new AtomicBoolean();
	/**服务器对外停止服务*/
	final AtomicBoolean serverClosed = new AtomicBoolean();
	// 服务判定离线时间
	public static final long SERVER_OFFLINE_SECONDS = 110L;
	/**
	 * 当前server node 的 info key
	 */
	private String REDIS_SERVER_NODE_INFO_KEY;
	/**
	 * 服务器的信息. 支持增加自定义字段.
	 */
	private ServerInfo currServerInfo;

	// redis
	private IRedisUtil redisUtil;

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

		node.getSender().addCloseListener("removeServerNode", (session, cause) -> {
			this.removeNode(node);
		});

		nodes.put(node.getServerId(), node);
	}

	synchronized void removeNode(ServerNode serverNode) {
		if (nodes.remove(serverNode.getServerId()) != null) {
			LoggerType.DUODUO_FLASH_HANDLER.info("====ServerId {} was removed!", serverNode.getServerId());
			serverNode.getSender().close(CloseCause.CHANNEL_CLOSE);
		}

	}

	/**
	 * 获得serverInfo
	 * @param serverId
	 * @return
	 */
	ServerInfo getServerInfo(int serverId) {
		String serverInfoStr = redisUtil.returnJedis().get(serverInfoRedisKey(serverId));
		if (StringUtil.isEmpty(serverInfoStr)) {
			throw new CustomException("ServerId [{}] is not online!", serverId);
		}

		return JsonUtil.getGeneralObj(serverInfoStr, ServerInfo.class);
	}

	/**
	 * 获得指定serverId的serverNode
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

		ServerInfo serverInfo = this.getServerInfo(serverId);
		if (serverInfo == null) {
			throw new CustomException("ID:{} ServerInfo absent!!", serverId);
		}
		// 目前服务器和服务器肯定是内网. 如果以后有多区域需要互通. 有两个解决方案:
		// 1. 让云服务器 跨区域搭内网
		// 2. 下面的getHost 修改为 getPublicHost
		return lockAndCreateServerNode(serverId, serverInfo.getHost(), serverInfo.getNodePort());
	}

	/**
	 * 锁定. 然后创建serverNode
	 * @param serverId
	 */
	private synchronized ServerNode lockAndCreateServerNode(int serverId, String host, int port) {
		if (nodes.containsKey(serverId)) {
			return nodes.get(serverId);
		}
		RedisLock redisLock = redisUtil.redisLock(createRedisKey(currServerInfo.getServerId(), serverId));
		try {
			if (redisLock.lock()) {
				if (nodes.containsKey(serverId)) {
					redisLock.unlock();
					return nodes.get(serverId);
				}
				return new ServerNode(redisLock, serverId, host, port);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		throw new CustomException("Create server node [{}] fail", serverId);
	}

	@Override
	public void setApplicationContext(IApplicationContext context, ArgsContainer argsContainer) throws Exception {
		this.currServerInfo = argsContainer.isNull(ScannerParamKey.CUSTOM_SERVER_INFO)
			? ServerInfo.valueOf(ServerConfig.getServerPort(), ServerConfig.getNodePort())
			: argsContainer.getArgument(ScannerParamKey.CUSTOM_SERVER_INFO).get();

		this.REDIS_SERVER_NODE_INFO_KEY = serverInfoRedisKey(currServerInfo.getServerId());

		Argument<Supplier<IRedisUtil>> redisArg = argsContainer.getArgument(ScannerParamKey.SERVER_NODE_REDIS_INSTANCE_SUPPLIER);
		if (! redisArg.isNull()) {
			this.redisUtil = redisArg.get().get();

			// 启动检测 redis 是否通畅.
			this.redisUtil.returnJedis().exists("");
		}

	}

	@EventListener
	private void onServerStart(ServerStartupEventData data){
		if (this.currServerInfo.getNodePort() == 0) {
			return;
		}

		redisUtil.returnJedis().sadd(serverRegisterCenterRedisKey(this.currServerInfo.getServerType()), String.valueOf(this.currServerInfo.getServerId()));
		TimerManager.executor.scheduleAtFixedRate(() -> {
			if (deprecated.get()) {
				return;
			}
			currServerInfo.put(ServerInfo.lastUpdateDt, System.currentTimeMillis());

			// 触发心跳 业务可能修改ServerInfo数据.
			ServerNodeTickEvent.instance.fireEventHandler();

			redisUtil.returnJedis(false).set(REDIS_SERVER_NODE_INFO_KEY, currServerInfo.toString(), SetParams.setParams().ex(SERVER_OFFLINE_SECONDS));
		}, MathUtil.random(0, 200), TimeUnit.SECONDS.toMillis(60), TimeUnit.MILLISECONDS);
	}

	ServerInfo getCurrServerInfo() {
		return currServerInfo;
	}

	/**
	 * 所有的某个类型的服务器ID 集合
	 * @param serverType
	 * @return
	 */
	private static String serverRegisterCenterRedisKey(ServerType serverType) {
		return "SERVER_REGISTER_CENTER#"+serverType;
	}


	/**
	 * 获得创建使用的key
	 * @param destServerId
	 * @return
	 */
	private static String createRedisKey(int srcServerId, int destServerId) {
		if (destServerId < srcServerId) {
			return SERVER_NODE_CREATE_SYNC_LOCK_KEY + destServerId +"_"+ srcServerId;
		}else {
			return SERVER_NODE_CREATE_SYNC_LOCK_KEY + srcServerId +"_"+  destServerId;
		}
	}

	/**
	 * server info的redis key
	 * @param serverId
	 * @return
	 */
	private static String serverInfoRedisKey(int serverId) {
		return "SERVER_NODE_INFO_REDIS_KEY#"+serverId;
	}

	@EventListener
	private void deprecatedEvent(ServerDeprecatedEvent event) {
		if (redisUtil == null) {
			return;
		}

		if (this.deprecated.compareAndSet(false, true)) {
			redisUtil.returnJedis().srem(serverRegisterCenterRedisKey(this.currServerInfo.getServerType()), String.valueOf(this.currServerInfo.getServerId()));
			redisUtil.returnJedis().del(REDIS_SERVER_NODE_INFO_KEY);
		}
	}

	@EventListener
	private void serverClosed(ServerClosedEvent event) {
		if (redisUtil == null) {
			return;
		}

		redisUtil.returnJedis().srem(serverRegisterCenterRedisKey(this.currServerInfo.getServerType()), String.valueOf(this.currServerInfo.getServerId()));
		this.serverClosed.set(true);
	}

	@Override
	public ScannerType scannerType() {
		return ScannerType.SERVER_NODE;
	}

	@EventListener(EventHandlerWeightType.MIDDLE)
	private void onShutdown(ServerShutdownEventData data) {
		if (redisUtil == null) {
			return;
		}

		redisUtil.execCommands(jedis -> {
			jedis.srem(serverRegisterCenterRedisKey(this.currServerInfo.getServerType()), String.valueOf(this.currServerInfo.getServerId()));
			jedis.del(REDIS_SERVER_NODE_INFO_KEY);
			return null;
		});

		nodes.values().forEach(node -> this.removeNode(node));
		NettyTcpClient.shutdown();
	}

	@Override
	public int order() {
		return 8;
	}


}
