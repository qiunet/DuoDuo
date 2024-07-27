package org.qiunet.cross.node;

import com.google.common.base.Preconditions;
import io.netty.buffer.ByteBuf;
import org.qiunet.data.util.ServerType;
import org.qiunet.flash.handler.context.request.data.IChannelData;
import org.qiunet.flash.handler.context.response.push.DefaultByteBufMessage;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;

/***
 *
 *
 * @author qiunet
 * 2020-10-21 17:21
 */
public class ServerNodeManager {
	/**
	 * 获得当前的serverId
	 * @return
	 */
	public static int getCurrServerId(){
		return getCurrServerInfo().getServerId();
	}

	/**
	 * 得到当前的ServerInfo
	 * @return
	 */
	public static ServerInfo getCurrServerInfo() {
		return ServerNodeManager0.instance.getCurrServerInfo();
	}
	/**
	 * 获得当前的serverType
	 * @return
	 */
	public static ServerType getCurrServerType(){
		return getCurrServerInfo().getServerType();
	}

	/**
	 * 是否是废弃服务器
	 * @return
	 */
	public static boolean isDeprecatedServer(){
		return ServerNodeManager0.instance.deprecated.get();
	}

	/**
	 * 是否对外停止服务.
	 * @return
	 */
	public static boolean isServerClosed(){
		return ServerNodeManager0.instance.serverClosed.get();
	}
	/**
	 * 获得serverInfo
	 * @param serverId
	 * @return
	 */
	public static ServerInfo getServerInfo(int serverId) {
		return ServerNodeManager0.instance.getServerInfo(serverId);
	}

	/**
	 * 是否过期失效的服务id
	 * @param serverId server id
	 * @return true 失效了
	 */
	public static boolean isDeprecateServerId(int serverId) {
		return serverId <= 0;
	}

	/**
	 * 获得 某个server 类型的所有的 server info
	 * @param type 类型
	 * @return 列表
	 */
	public static List<ServerInfo> getServerInfoList(ServerType type) {
		return ServerNodeManager0.instance.getServerInfoList(type);
	}
	/**
	 * 获得一个serverNode
	 * @param serverId
	 * @return
	 */
	public static void getNode(int serverId, Consumer<ServerNode> consumer) {
		ServerNodeManager0.instance.getNode(serverId, consumer);
	}
	/**
	 * 根据server type 以及filter
	 * @param serverType 服务类型
	 * @return 最终的所有server id
	 */
	public static List<ServerInfo> serverList(ServerType serverType) {
		return serverList(serverType, t -> true);
	}
	/**
	 * 根据server type 以及filter
	 * @param serverType 服务类型
	 * @param filter 过滤器 true的才保留
	 * @return 最终的所有server id
	 */
	public static List<ServerInfo> serverList(ServerType serverType, Predicate<Integer> filter) {
		return ServerNodeManager0.instance.serverList(serverType, filter);
	}
	/**
	 * 根据server type 以及filter
	 * @param serverList 获取server list . {@link #serverList(ServerType, Predicate)}
	 * @return 最终的所有server id
	 */
	public static ServerInfo assignServer(List<ServerInfo> serverList) {
		return ServerNodeManager0.instance.assignServer(serverList, t -> true);
	}
	/**
	 * 根据server type 以及filter
	 * @param serverList 获取server list . {@link #serverList(ServerType, Predicate)}
	 * @param filter 过滤器 true的才保留
	 * @return 最终的所有server id
	 */
	public static ServerInfo assignServer(List<ServerInfo> serverList, Predicate<ServerInfo> filter) {
		return ServerNodeManager0.instance.assignServer(serverList, filter);
	}
	/**
	 * 在Logic 按照group 分配一个权重高机器
	 * @param groupId server group id
	 * @return 权重高的info
	 */
	public static ServerInfo assignLogicServerByGroupId(int groupId) {
		return assignLogicServerByGroupId(groupId, t -> true);
	}
	/**
	 * 在Logic 按照group 分配一个权重高机器
	 * @param groupId server group id
	 * @param filter 过滤器
	 * @return 权重高的info
	 */
	public static ServerInfo assignLogicServerByGroupId(int groupId, Predicate<ServerInfo> filter) {
		return assignServer(serverList(ServerType.LOGIC, id -> ServerType.getGroupId(id) == groupId), filter);
	}
	/**
	 * 在指定server type里面找一个weight最高的服务.
	 * @param serverType server type
	 * @return 权重高的info
	 */
	public static ServerInfo assignServer(ServerType serverType) {
		return assignServer(serverType, i -> true);
	}
	/**
	 * 在指定server type里面找一个weight最高的服务.
	 * @param serverType server type
	 * @param filter 过滤器
	 * @return 权重高的info
	 */
	public static ServerInfo assignServer(ServerType serverType, Predicate<ServerInfo> filter) {
		return assignServer(serverList(serverType), filter);
	}
	/**
	 * 对指定类型的服务器进行广播
	 * @param serverType 类型
	 * @param filter 过滤器
	 * @param channelData 广播内容
	 */
	public static void broadcast(ServerType serverType, Predicate<Integer> filter, IChannelData channelData) {
		Preconditions.checkNotNull(filter);
		ByteBuf byteBuf = null;
		try {
			List<Integer> serveredIdList = ServerNodeManager0.instance.serverIdList(serverType, filter);
			int protocolId = channelData.protocolId();
			byteBuf = channelData.toByteBuf();
			for (Integer serverId : serveredIdList) {
				DefaultByteBufMessage message = DefaultByteBufMessage.valueOf(protocolId, byteBuf.retainedDuplicate());
				getNode(serverId, node ->node.sendMessage(message));
			}
		}finally {
			if (byteBuf != null) {
				byteBuf.release();
			}
		}
	}
	/**
	 * 对指定类型的服务器进行广播
	 * 包含 失效服务
	 * @param serverType 类型
	 * @param channelData 广播内容
	 */
	public static void broadcast(ServerType serverType, IChannelData channelData) {
		broadcast(serverType, t -> true, channelData);
	}
}
