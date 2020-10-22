package org.qiunet.cross.node;

import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;
import org.qiunet.cross.common.config.ServerConfig;
import org.qiunet.cross.common.contants.ScannerParamKey;
import org.qiunet.data.core.support.redis.IRedisUtil;
import org.qiunet.data.util.DbProperties;
import org.qiunet.data.util.ServerType;
import org.qiunet.flash.handler.netty.client.tcp.NettyTcpClient;
import org.qiunet.flash.handler.netty.server.constants.CloseCause;
import org.qiunet.listener.event.EventListener;
import org.qiunet.listener.event.data.ServerShutdownEventData;
import org.qiunet.listener.event.data.ServerStartupEventData;
import org.qiunet.utils.args.ArgsContainer;
import org.qiunet.utils.exceptions.CustomException;
import org.qiunet.utils.json.JsonUtil;
import org.qiunet.utils.scanner.IApplicationContext;
import org.qiunet.utils.scanner.IApplicationContextAware;
import org.qiunet.utils.string.StringUtil;
import org.qiunet.utils.timer.TimerManager;

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

	private ServerInfo currServerInfo;

	private static final Map<Integer, ServerNode> nodes = Maps.newConcurrentMap();

	/**
	 * 添加一个服务器节点
	 * @param node
	 */
	void addNode(ServerNode node) {
		Preconditions.checkState(node.isAuth(), "ServerNode need auth");
		nodes.put(node.getServerId(), node);
	}

	ServerNode getNode(int serverId) {
		return nodes.computeIfAbsent(serverId, key -> {
			String serverInfoStr = redisUtil.returnJedis().get(serverInfoRedisKey(key));
			if (StringUtil.isEmpty(serverInfoStr)) {
				throw new CustomException("ServerId {} is not online!", serverId);
			}

			ServerInfo serverInfo = JsonUtil.getGeneralObject(serverInfoStr, ServerInfo.class);
			return ServerNode.valueOf(serverInfo);
		});
	}

	@Override
	public void setApplicationContext(IApplicationContext context, ArgsContainer argsContainer) throws Exception {
		if (DbProperties.getInstance().getServerType() == ServerType.ALL) {
			return;
		}

		if (argsContainer.getArgument(ScannerParamKey.SERVER_NODE_REDIS_INSTANCE).isEmpty()) {
			throw new CustomException("Need Specify Redis Instance with key 'ScannerParamKey.SERVER_NODE_REDIS_INSTANCE'");
		}

		this.currServerInfo = argsContainer.isEmpty(ScannerParamKey.CUSTOM_SERVER_INFO)
			? ServerInfo.valueOf(ServerConfig.getCommunicationPort())
			: argsContainer.getArgument(ScannerParamKey.CUSTOM_SERVER_INFO).get();

		this.redisUtil = argsContainer.getArgument(ScannerParamKey.SERVER_NODE_REDIS_INSTANCE).get();
	}

	@EventListener
	public void onServerStart(ServerStartupEventData data){
		if (DbProperties.getInstance().getServerType() == ServerType.ALL) {
			return;
		}

		TimerManager.executor.scheduleAtFixedRate(this::refreshServerInfo, 0, 1, TimeUnit.MINUTES);
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

	@EventListener
	public void onShutdown(ServerShutdownEventData data) {
		nodes.values().forEach(node -> node.getSession().close(CloseCause.SERVER_SHUTDOWN));
		NettyTcpClient.shutdown();
	}
}
