package org.qiunet.cross.node;

import com.google.common.collect.Maps;
import io.netty.channel.Channel;
import org.qiunet.cross.common.contants.ScannerParamKey;
import org.qiunet.cross.pool.NodeChannelPool;
import org.qiunet.cross.pool.NodeChannelPoolMap;
import org.qiunet.cross.pool.NodeChannelTrigger;
import org.qiunet.data.core.support.redis.IRedisUtil;
import org.qiunet.data.util.ServerConfig;
import org.qiunet.data.util.ServerType;
import org.qiunet.flash.handler.common.enums.ServerConnType;
import org.qiunet.flash.handler.context.header.INodeServerHeader;
import org.qiunet.flash.handler.context.session.ISession;
import org.qiunet.flash.handler.context.session.NodeClientSession;
import org.qiunet.flash.handler.context.session.NodeSessionType;
import org.qiunet.flash.handler.netty.server.constants.CloseCause;
import org.qiunet.flash.handler.netty.server.constants.ServerConstants;
import org.qiunet.flash.handler.netty.server.event.ServerStartupCompleteEvent;
import org.qiunet.utils.args.ArgsContainer;
import org.qiunet.utils.args.Argument;
import org.qiunet.utils.exceptions.CustomException;
import org.qiunet.utils.json.JsonUtil;
import org.qiunet.utils.listener.event.EventHandlerWeightType;
import org.qiunet.utils.listener.event.EventListener;
import org.qiunet.utils.listener.event.data.ServerClosedEvent;
import org.qiunet.utils.listener.event.data.ServerDeprecatedEvent;
import org.qiunet.utils.listener.event.data.ServerShutdownEvent;
import org.qiunet.utils.logger.LoggerType;
import org.qiunet.utils.math.MathUtil;
import org.qiunet.utils.scanner.IApplicationContext;
import org.qiunet.utils.scanner.IApplicationContextAware;
import org.qiunet.utils.scanner.ScannerType;
import org.qiunet.utils.string.StringUtil;
import org.qiunet.utils.timer.TimerManager;
import redis.clients.jedis.params.SetParams;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;
import java.util.function.Predicate;
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
enum ServerNodeManager0 implements IApplicationContextAware, NodeChannelTrigger {
	instance;
	/**
	 * poolMap
	 */
	private final NodeChannelPoolMap poolMap = new NodeChannelPoolMap(this, 2);
	/** 注册中心redis key 前缀 with server type */
	private static final String SERVER_REGISTER_CENTER_PREFIX = "SERVER_REGISTER_CENTER#";
	// 客户端当前的节点
	final Map<Integer, ServerNode> nodes = Maps.newConcurrentMap();
	/** 服务器已经过期. 不再上传信息 . login 不再分配进入.*/
	final AtomicBoolean deprecated = new AtomicBoolean();
	/**服务器对外停止服务*/
	final AtomicBoolean serverClosed = new AtomicBoolean();
	/**
	 * 当前server node 的 info key
	 */
	private String CURRENT_SERVER_NODE_INFO_REDIS_KEY;
	/**
	 * 服务器的信息. 支持增加自定义字段.
	 */
	private ServerInfo currServerInfo;

	// redis
	private IRedisUtil redisUtil;

	/**
	 * 获得serverInfo
	 * @param serverId
	 * @return
	 */
	ServerInfo getServerInfo(int serverId) {
		String serverInfoStr = redisUtil.returnJedis().get(ServerInfo.serverInfoRedisKey(serverId));
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
	void getNode(int serverId, Consumer<ServerNode> consumer) {
		if (serverId <= 0) {
			throw new CustomException("serverId not a valid value!");
		}

		if (serverId == currServerInfo.getServerId()) {
			throw new CustomException("It is current server!!");
		}

		ServerNode serverNode = nodes.get(serverId);
		if (serverNode != null) {
			consumer.accept(serverNode);
			return;
		}

		ServerInfo serverInfo = this.getServerInfo(serverId);
		if (serverInfo == null) {
			throw new CustomException("ID:{} ServerInfo absent!!", serverId);
		}

		ServerNode serverNode0 = this.createServerNode(serverInfo);
		consumer.accept(serverNode0);
	}
	/**
	 * 锁定. 然后创建serverNode
	 * @param serverInfo
	 */
	private synchronized ServerNode createServerNode(ServerInfo serverInfo) {
		ServerNode serverNode = nodes.get(serverInfo.getServerId());
		if (serverNode != null) {
			return serverNode;
		}
		NodeChannelPool pool = poolMap.get(serverInfo.getServerId());
		NodeClientSession session = new NodeClientSession(NodeSessionType.SERVER_NODE, pool, currServerInfo.getServerId());
		session.attachObj(ServerConstants.HANDLER_TYPE_KEY, ServerConnType.TCP);
		ServerNode node = new ServerNode(session, serverInfo.getServerId());
		session.attachObj(ServerConstants.MESSAGE_ACTOR_KEY, node);
		node.getSession().addCloseListener("removeServerNode", (s, cause) -> {
			ServerNode node0;
			if ((node0 = nodes.remove(serverInfo.getServerId())) != null) {
				LoggerType.DUODUO_FLASH_HANDLER.info("====Server Node Client ServerId {} was removed!", serverInfo.getServerId());
				if (!((NodeClientSession) node0.getSession()).isNoticedRemote()) {
					((NodeClientSession) node0.getSession()).setNoticedRemote();
					node0.fireCrossEvent(ServerNodeQuitEvent.valueOf());
				}
			}
		});
		nodes.put(serverInfo.getServerId(), node);
		return node;
	}

	@Override
	public void setApplicationContext(IApplicationContext context, ArgsContainer argsContainer) throws Exception {
		this.currServerInfo = argsContainer.isNull(ScannerParamKey.CUSTOM_SERVER_INFO)
			? ServerInfo.selfInfo(ServerConfig.getServerPort(), ServerConfig.getNodePort())
			: argsContainer.getArgument(ScannerParamKey.CUSTOM_SERVER_INFO).get();

		this.CURRENT_SERVER_NODE_INFO_REDIS_KEY = ServerInfo.serverInfoRedisKey(currServerInfo.getServerId());

		Argument<Supplier<IRedisUtil>> redisArg = argsContainer.getArgument(ScannerParamKey.SERVER_NODE_REDIS_INSTANCE_SUPPLIER);
		if (! redisArg.isNull()) {
			this.redisUtil = redisArg.get().get();

			// 启动检测 redis 是否通畅.
			this.redisUtil.returnJedis().exists("");
		}
	}

	@EventListener
	private void onServerStart(ServerStartupCompleteEvent data){
		if (this.currServerInfo.getNodePort() == 0) {
			return;
		}

		redisUtil.returnJedis().sadd(serverRegisterCenterRedisKey(this.currServerInfo.getServerType()), String.valueOf(this.currServerInfo.getServerId()));
		TimerManager.executor.scheduleAtFixedRate(() -> {
			if (! deprecated.get() && !serverClosed.get()) {
				// 触发心跳 业务可能修改ServerInfo数据.
				currServerInfo.refreshUpdateDt();
				ServerNodeTickEvent.instance.fireEventHandler();
			}
			this.addCurrentServerInfoToRedis();
		}, MathUtil.random(0, 200), TimeUnit.SECONDS.toMillis(60), TimeUnit.MILLISECONDS);
	}
	/**
	 * 根据server type 以及filter 获得所有的serverId
	 * @param serverType 服务类型
	 * @param filter 过滤器 true的才保留
	 * @return 最终的所有server id
	 */
	List<Integer> serverIdList(ServerType serverType, Predicate<Integer> filter) {
		String redisKey = serverRegisterCenterRedisKey(serverType);
		Set<String> members = redisUtil.returnJedis().smembers(redisKey);
		return members.stream().map(Integer::parseInt)
			.filter(filter).collect(Collectors.toList());
	}
	/**
	 * 根据server type 以及filter 获得server info
	 * @param serverType 服务类型
	 * @param filter 过滤器 true的才保留
	 * @return 最终的所有server info
	 */
	List<ServerInfo> serverList(ServerType serverType, Predicate<Integer> filter) {
		return serverIdList(serverType, filter).stream().map(this::getServerInfo)
			.collect(Collectors.toList());
	}

	/**
	 * 根据server type 以及filter
	 * @param serverIdList 获取server list . {@link #serverList(ServerType, Predicate)}
	 * @param filter 过滤器 true的才保留
	 * @return 最终的所有server id
	 */
	ServerInfo assignServer(List<ServerInfo> serverIdList, Predicate<ServerInfo> filter) {
		if (serverIdList == null || serverIdList.isEmpty()) {
			return null;
		}
		return serverIdList.stream()
			.filter(filter).reduce((o1, o2) -> {
				if (o1.weight() > o2.weight()) return o1;
				return o2;
			}).orElseThrow(() -> new CustomException("Can not assign server!"));
	}

	ServerInfo getCurrServerInfo() {
		return currServerInfo;
	}

	/**
	 * 获得 某个server 类型的所有的 server info
	 * @param type 类型
	 * @return 列表
	 */
	List<ServerInfo> getServerInfoList(ServerType type) {
		String registerCenterRedisKey = serverRegisterCenterRedisKey(type);
		Set<String> members = redisUtil.returnJedis().smembers(registerCenterRedisKey);
		return redisUtil.execCommands(jedis -> {
			List<ServerInfo> infos = new ArrayList<>(members.size());
			for (String member : members) {
				String infoString = jedis.get(ServerInfo.serverInfoRedisKey(member));
				if (StringUtil.isEmpty(infoString)) {
					continue;
				}
				ServerInfo serverInfo = JsonUtil.getGeneralObj(infoString, ServerInfo.class);
				if (serverInfo.isOffline()) {
					continue;
				}
 				infos.add(serverInfo);
			}
			return infos;
		});
	}

	/**
	 * 所有的某个类型的服务器ID 集合
	 * @param serverType
	 * @return
	 */
	private String serverRegisterCenterRedisKey(ServerType serverType) {
		return SERVER_REGISTER_CENTER_PREFIX +serverType;
	}

	/**
	 * 将serverInfo添加到redis
	 */
	private void addCurrentServerInfoToRedis() {
		redisUtil.returnJedis(false).set(CURRENT_SERVER_NODE_INFO_REDIS_KEY, currServerInfo.toString(), SetParams.setParams().ex(ServerInfo.SERVER_OFFLINE_SECONDS));
	}
	@EventListener
	private void deprecatedEvent(ServerDeprecatedEvent event) {
		if (redisUtil == null) {
			return;
		}

		if (this.deprecated.compareAndSet(false, true)) {
			redisUtil.returnJedis().srem(serverRegisterCenterRedisKey(this.currServerInfo.getServerType()), String.valueOf(this.currServerInfo.getServerId()));
			this.currServerInfo.setDeprecate();
			this.addCurrentServerInfoToRedis();
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

	@EventListener
	private void serverNodeQuitEvent(ServerNodeQuitEvent event) {
		ServerNode serverNode = nodes.remove(event.getServerId());
		if (serverNode != null) {
			serverNode.getSession().close(CloseCause.LOGOUT);
		}
	}

	@EventListener(EventHandlerWeightType.MIDDLE)
	private void onShutdown(ServerShutdownEvent data) {
		if (redisUtil == null) {
			return;
		}

		redisUtil.execCommands(jedis -> {
			jedis.srem(serverRegisterCenterRedisKey(this.currServerInfo.getServerType()), String.valueOf(this.currServerInfo.getServerId()));
			jedis.del(CURRENT_SERVER_NODE_INFO_REDIS_KEY);
			return null;
		});

		nodes.values().forEach(serverNode -> {
			serverNode.getSession().close(CloseCause.SERVER_SHUTDOWN);
		});
	}

	@Override
	public int order() {
		return 8;
	}


	@Override
	public boolean serverNode() {
		return true;
	}

	@Override
	public ISession getNodeSession(Channel channel, INodeServerHeader header) {
		return nodes.get((int)header.id()).getSession();
	}
}
