package org.qiunet.cross.node;

import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;
import io.netty.util.AttributeKey;
import org.qiunet.cross.common.contants.ScannerParamKey;
import org.qiunet.data.core.support.redis.IRedisUtil;
import org.qiunet.data.util.ServerConfig;
import org.qiunet.data.util.ServerType;
import org.qiunet.flash.handler.netty.client.tcp.NettyTcpClient;
import org.qiunet.flash.handler.netty.server.constants.CloseCause;
import org.qiunet.utils.args.ArgsContainer;
import org.qiunet.utils.async.future.DPromise;
import org.qiunet.utils.exceptions.CustomException;
import org.qiunet.utils.json.JsonUtil;
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

import java.util.Map;
import java.util.concurrent.TimeUnit;

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
	private static final AttributeKey<DPromise<ServerNode>> SERVER_NODE_PROMISE_ATTRIBUTE = AttributeKey.newInstance("SERVER_NODE_PROMISE_ATTRIBUTE");

	private final Logger logger = LoggerType.DUODUO_CROSS.getLogger();
	/***
	 * server Node 在redis中的 key
	 * 主要查找某一个类型所有服务
	 */
	private static final String SERVER_NODE_REDIS_SET_KEY = "SERVER_NODE_REDIS_SET_KEY";
	/***
	 * server Node 在redis中的 key
	 * 主要查找某一个指定id服务
	 */
	private static final String SERVER_NODE_INFO_REDIS_KEY = "SERVER_NODE_INFO_REDIS_KEY";

	private IRedisUtil redisUtil;
	/**
	 * 服务器的信息. 支持增加自定义字段.
	 */
	private ServerInfo currServerInfo;

	private static final Map<Integer, ServerNode> nodes = Maps.newConcurrentMap();

	/**
	 * 添加一个服务器节点
	 * @param node
	 */
	synchronized boolean addNode(ServerNode node) {
		Preconditions.checkState(node.isAuth(), "ServerNode need auth");
		ServerNode serverNode = nodes.get(node.getServerId());
		if (serverNode != null && serverNode.getSession().isActive()) {
			return false;
		}

		if (serverNode != null) {
			serverNode.getSession().close(CloseCause.INACTIVE);
		}

		nodes.put(node.getServerId(), node);
		node.getSession().addCloseListener(cause -> nodes.remove(node.getServerId()));
		return true;
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

		return JsonUtil.getGeneralObject(serverInfoStr, ServerInfo.class);
	}

	/**
	 * 获得serverNode
	 * @param serverId
	 * @return
	 */
	ServerNode getNode(int serverId) {
		return nodes.computeIfAbsent(serverId, key -> new ServerNode(this.getServerInfo(key)));
	}

	@Override
	public int order() {
		return 1;
	}

	@Override
	public void setApplicationContext(IApplicationContext context, ArgsContainer argsContainer) throws Exception {
		this.currServerInfo = argsContainer.isNull(ScannerParamKey.CUSTOM_SERVER_INFO)
			? ServerInfo.valueOf(ServerConfig.getServerPort(), ServerConfig.getNodePort())
			: argsContainer.getArgument(ScannerParamKey.CUSTOM_SERVER_INFO).get();

		if (currServerInfo.getType() == ServerType.ALL) {
			return;
		}

		if (argsContainer.getArgument(ScannerParamKey.SERVER_NODE_REDIS_INSTANCE_SUPPLIER).isNull()) {
			throw new CustomException("Need Specify Redis Instance with key 'ScannerParamKey.SERVER_NODE_REDIS_INSTANCE_SUPPLIER'");
		}


		this.redisUtil = argsContainer.getArgument(ScannerParamKey.SERVER_NODE_REDIS_INSTANCE_SUPPLIER).get().get();
		// 启动检测 redis 是否通畅.
		this.redisUtil.returnJedis().exists("");
	}

	@EventListener
	public void onServerStart(ServerStartupEventData data){
		if (currServerInfo.getType() == ServerType.ALL) {
			return;
		}
		int delay = MathUtil.random(0, 200);
		TimerManager.executor.scheduleAtFixedRate(this::refreshServerInfo, delay, 60000, TimeUnit.MILLISECONDS);
	}

	private String serverInfoRedisKey(int serverId) {
		return SERVER_NODE_INFO_REDIS_KEY + serverId;
	}

	ServerInfo getCurrServerInfo() {
		return currServerInfo;
	}

	/**
	 * 每一分钟, 刷新server info
	 */
	public void refreshServerInfo() {
		redisUtil.execCommands(jedis -> {
			jedis.setex(serverInfoRedisKey(currServerInfo.getServerId()), 110, JsonUtil.toJsonString(currServerInfo));
			jedis.sadd(SERVER_NODE_REDIS_SET_KEY+currServerInfo.getType().getType(), String.valueOf(currServerInfo.getServerId()));
			return 0;
		});
	}

	@Override
	public ScannerType scannerType() {
		return ScannerType.SERVER;
	}

	@EventListener
	public void onShutdown(ServerShutdownEventData data) {
		nodes.values().forEach(node -> node.getSession().close(CloseCause.SERVER_SHUTDOWN));
		NettyTcpClient.shutdown();
	}
}
