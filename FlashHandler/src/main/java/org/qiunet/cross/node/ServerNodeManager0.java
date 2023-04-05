package org.qiunet.cross.node;

import com.google.common.collect.Maps;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.epoll.Epoll;
import io.netty.channel.epoll.EpollSocketChannel;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import org.qiunet.cross.common.contants.ScannerParamKey;
import org.qiunet.cross.pool.NodeChannelPoolHandler;
import org.qiunet.data.core.support.redis.IRedisUtil;
import org.qiunet.data.util.ServerConfig;
import org.qiunet.data.util.ServerType;
import org.qiunet.flash.handler.common.enums.ServerConnType;
import org.qiunet.flash.handler.context.session.NodeSessionType;
import org.qiunet.flash.handler.context.session.ServerNodeSession;
import org.qiunet.flash.handler.netty.server.constants.CloseCause;
import org.qiunet.flash.handler.netty.server.constants.ServerConstants;
import org.qiunet.utils.args.ArgsContainer;
import org.qiunet.utils.args.Argument;
import org.qiunet.utils.exceptions.CustomException;
import org.qiunet.utils.json.JsonUtil;
import org.qiunet.utils.listener.event.EventHandlerWeightType;
import org.qiunet.utils.listener.event.EventListener;
import org.qiunet.utils.listener.event.data.ServerClosedEvent;
import org.qiunet.utils.listener.event.data.ServerDeprecatedEvent;
import org.qiunet.utils.listener.event.data.ServerShutdownEvent;
import org.qiunet.utils.listener.event.data.ServerStartupEvent;
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
	/** 注册中心redis key 前缀 with server type */
	private static final String SERVER_REGISTER_CENTER_PREFIX = "SERVER_REGISTER_CENTER#";
	// 客户端当前的节点
	final Map<Integer, ServerNode> nodes = Maps.newConcurrentMap();
	/** 服务器已经过期. 不再上传信息 . login 不再分配进入.*/
	final AtomicBoolean deprecated = new AtomicBoolean();
	/**服务器对外停止服务*/
	final AtomicBoolean serverClosed = new AtomicBoolean();
	/***自己的bootstrap*/
	final Bootstrap bootstrap = bootstrap();

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
		return nodes.computeIfAbsent(serverId, key -> this.createServerNode(serverInfo));
	}

	/**
	 * 获得一个Bootstrap
	 * @return
	 */
	private static Bootstrap bootstrap() {
		Bootstrap bootstrap = new Bootstrap();
		NodeChannelPoolHandler nodeChannelPoolHandler = new NodeChannelPoolHandler(new ServerNodeClientTrigger(), 8192);
		Class<? extends SocketChannel> socketChannelClz = Epoll.isAvailable() ? EpollSocketChannel.class : NioSocketChannel.class;
		bootstrap.handler(new ChannelInitializer<>() {
			@Override
			protected void initChannel(Channel ch) throws Exception {
				nodeChannelPoolHandler.channelCreated(ch);
			}
		});
		bootstrap.option(ChannelOption.TCP_NODELAY,true);
		bootstrap.group(ServerConstants.WORKER);
		bootstrap.channel(socketChannelClz);
		return bootstrap;
	}
	/**
	 * 锁定. 然后创建serverNode
	 * @param serverInfo
	 */
	private ServerNode createServerNode(ServerInfo serverInfo) {
		Channel channel;
		try {
			ChannelFuture channelFuture = bootstrap.connect(serverInfo.getHost(), serverInfo.getNodePort()).sync();
			channel = channelFuture.channel();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		ServerNodeSession session = new ServerNodeSession(NodeSessionType.SERVER_NODE, channel, currServerInfo.getServerId());
		session.attachObj(ServerConstants.HANDLER_TYPE_KEY, ServerConnType.TCP);
		ServerNode node = new ServerNode(session, serverInfo.getServerId());
		node.getSession().addCloseListener("removeServerNode", (s, cause) -> {
			ServerNode node0;
			if ((node0 = nodes.remove(serverInfo.getServerId())) != null) {
				LoggerType.DUODUO_FLASH_HANDLER.info("====Server Node Client ServerId {} was removed!", serverInfo.getServerId());
				if (!((ServerNodeSession) node0.getSession()).isNoticedRemote()) {
					((ServerNodeSession) node0.getSession()).setNoticedRemote();
					node0.fireCrossEvent(ServerNodeQuitEvent.valueOf());
				}
			}
		});
		return node;
	}

	@Override
	public void setApplicationContext(IApplicationContext context, ArgsContainer argsContainer) throws Exception {
		this.currServerInfo = argsContainer.isNull(ScannerParamKey.CUSTOM_SERVER_INFO)
			? ServerInfo.valueOf(ServerConfig.getServerPort(), ServerConfig.getNodePort())
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
	private void onServerStart(ServerStartupEvent data){
		if (this.currServerInfo.getNodePort() == 0) {
			return;
		}

		redisUtil.returnJedis().sadd(serverRegisterCenterRedisKey(this.currServerInfo.getServerType()), String.valueOf(this.currServerInfo.getServerId()));
		TimerManager.executor.scheduleAtFixedRate(() -> {
			// 触发心跳 业务可能修改ServerInfo数据.
			currServerInfo.refreshUpdateDt();
			ServerNodeTickEvent.instance.fireEventHandler();
			redisUtil.returnJedis(false).set(CURRENT_SERVER_NODE_INFO_REDIS_KEY, currServerInfo.toString(), SetParams.setParams().ex(ServerInfo.SERVER_OFFLINE_SECONDS));
		}, MathUtil.random(0, 200), TimeUnit.SECONDS.toMillis(60), TimeUnit.MILLISECONDS);
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

	@EventListener
	private void deprecatedEvent(ServerDeprecatedEvent event) {
		if (redisUtil == null) {
			return;
		}

		if (this.deprecated.compareAndSet(false, true)) {
			redisUtil.returnJedis().srem(serverRegisterCenterRedisKey(this.currServerInfo.getServerType()), String.valueOf(this.currServerInfo.getServerId()));
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
}
