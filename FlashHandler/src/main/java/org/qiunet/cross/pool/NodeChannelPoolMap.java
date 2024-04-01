package org.qiunet.cross.pool;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelOption;
import io.netty.channel.epoll.Epoll;
import io.netty.channel.epoll.EpollSocketChannel;
import io.netty.channel.pool.AbstractChannelPoolMap;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import org.qiunet.cross.node.ServerInfo;
import org.qiunet.cross.node.ServerNodeManager;
import org.qiunet.flash.handler.netty.server.constants.ServerConstants;
import org.qiunet.utils.listener.hook.ShutdownHookUtil;

/***
 * Server node to Server node
 * channel 缓存
 *
 * @author qiunet
 * 2023/3/23 16:27
 */
public class NodeChannelPoolMap extends AbstractChannelPoolMap<Integer, NodeChannelPool> {
	/**
	 * 最大持有连接数量
	 */
	private static final int DEFAULT_MAX_CONNECTIONS = 20;
	/**
	 * trigger
	 */
	private final NodeChannelTrigger channelTrigger;
	/**
	 *  最大接收长度
	 */
	private final int maxMsgLength;
	/**
	 * 每个池最大维护多少channel
	 */
	private final int maxConnections;

	/**
	 *  使用默认的参数构建Channel Pool
	 */
	public NodeChannelPoolMap(NodeChannelTrigger channelTrigger, int maxConnections) {
		this(channelTrigger, ServerConstants.MAX_SOCKET_MESSAGE_LENGTH, maxConnections);
	}

	public NodeChannelPoolMap(NodeChannelTrigger channelTrigger) {
		this(channelTrigger, ServerConstants.MAX_SOCKET_MESSAGE_LENGTH, DEFAULT_MAX_CONNECTIONS);
	}
	/**
	 *  使用指定的参数构建Channel Pool
	 */
	public NodeChannelPoolMap(NodeChannelTrigger channelTrigger, int maxMsgLength, int maxConnections) {
		ShutdownHookUtil.getInstance().addShutdownHook(this::close);
		this.maxMsgLength = maxMsgLength;
		this.maxConnections = maxConnections;
		this.channelTrigger = channelTrigger;
	}

	@Override
	protected NodeChannelPool newPool(Integer key) {
		ServerInfo serverInfo = ServerNodeManager.getServerInfo(key);

		Bootstrap bootstrap = new Bootstrap();
		Class<? extends SocketChannel> socketChannelClz = Epoll.isAvailable() ? EpollSocketChannel.class : NioSocketChannel.class;
		bootstrap.remoteAddress(serverInfo.getHost(), serverInfo.getNodePort());
		bootstrap.option(ChannelOption.TCP_NODELAY,true);
		bootstrap.group(ServerConstants.WORKER);
		bootstrap.channel(socketChannelClz);

		return new NodeChannelPool(bootstrap, channelTrigger, true, maxMsgLength, maxConnections);
	}


}
