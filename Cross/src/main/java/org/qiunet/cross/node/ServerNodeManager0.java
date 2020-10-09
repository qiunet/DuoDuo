package org.qiunet.cross.node;

import com.google.common.collect.Maps;
import org.qiunet.cross.common.contants.ScannerParamKey;
import org.qiunet.data.core.support.redis.IRedisUtil;
import org.qiunet.listener.event.EventListener;
import org.qiunet.utils.args.ArgsContainer;
import org.qiunet.utils.exceptions.CustomException;
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
	private IRedisUtil redisUtil;

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
		if (argsContainer.getArgument(ScannerParamKey.SERVER_NODE_REDIS_INSTANCE).isEmpty()) {
			throw new CustomException("Need Specify Redis Instance with key 'ScannerParamKey.SERVER_NODE_REDIS_INSTANCE'");
		}
		this.redisUtil = argsContainer.getArgument(ScannerParamKey.SERVER_NODE_REDIS_INSTANCE).get();
		this.context = context;
	}

	@EventListener
	public void onServerStart(){
		TimerManager.executor.scheduleAtFixedRate(() -> {

		}, 0, 1, TimeUnit.MINUTES);
	}

	/**
	 * 每一分钟, 刷新server info
	 */
	public void refreshServerInfo() {

	}
}
