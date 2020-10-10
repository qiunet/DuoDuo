package org.qiunet.cross.node;

import com.google.common.collect.Maps;
import org.qiunet.cross.common.config.ServerConfig;
import org.qiunet.cross.common.contants.ScannerParamKey;
import org.qiunet.data.core.support.redis.IRedisUtil;
import org.qiunet.data.util.DbProperties;
import org.qiunet.data.util.ServerType;
import org.qiunet.listener.event.EventListener;
import org.qiunet.utils.args.ArgsContainer;
import org.qiunet.utils.exceptions.CustomException;
import org.qiunet.utils.json.JsonUtil;
import org.qiunet.utils.net.NetUtil;
import org.qiunet.utils.scanner.IApplicationContext;
import org.qiunet.utils.scanner.IApplicationContextAware;
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

	private ServerInfo serverInfo;

	private IApplicationContext context;

	private static final Map<Integer, ServerNode> nodes = Maps.newConcurrentMap();

	/**
	 * 添加一个服务器节点
	 * @param node
	 */
	void addNode(ServerNode node) {
		nodes.put(node.getServerId(), node);
	}



	@Override
	public void setApplicationContext(IApplicationContext context, ArgsContainer argsContainer) throws Exception {
		if (DbProperties.getInstance().getServerType() == ServerType.ALL) {
			return;
		}

		if (argsContainer.getArgument(ScannerParamKey.SERVER_NODE_REDIS_INSTANCE).isEmpty()) {
			throw new CustomException("Need Specify Redis Instance with key 'ScannerParamKey.SERVER_NODE_REDIS_INSTANCE'");
		}

		this.serverInfo = ServerInfo.valueOf(DbProperties.getInstance().getServerId(), DbProperties.getInstance().getServerType(),
			NetUtil.getInnerIp(), ServerConfig.getCommnicationPort());

		this.redisUtil = argsContainer.getArgument(ScannerParamKey.SERVER_NODE_REDIS_INSTANCE).get();
		this.context = context;
	}

	@EventListener
	public void onServerStart(){
		if (DbProperties.getInstance().getServerType() == ServerType.ALL) {
			return;
		}

		TimerManager.executor.scheduleAtFixedRate(this::refreshServerInfo, 0, 1, TimeUnit.MINUTES);
	}

	/**
	 * 每一分钟, 刷新server info
	 */
	public void refreshServerInfo() {
		redisUtil.execCommands(jedis -> {
			jedis.setex(SERVER_NODE_INFO_REDIS_KEY + serverInfo.getServerId(), 110, JsonUtil.toJsonString(serverInfo));
			jedis.sadd(SERVER_NODE_REDIS_SET_KEY+serverInfo.getType().getType(), String.valueOf(serverInfo.getServerId()));
			return 0;
		});
	}
}
