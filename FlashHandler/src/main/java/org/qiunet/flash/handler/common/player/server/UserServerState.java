package org.qiunet.flash.handler.common.player.server;

import org.qiunet.cross.node.ServerNodeManager;

/***
 * 对象一直保存在redis. 不失效.
 *
 * @author qiunet
 * 2023/5/10 08:23
 */
public class UserServerState {
	private static final String REDIS_PREFIX_KEY = "duo-duo_user_server_state_";
	transient String redisKey;
	transient long playerId;
	/**
	 * 服务组id
	 * 注册后不再变动
	 */
	private int groupId;
	/**
	 * 不在线 也没有离线actor 为0
	 */
	private int serverId;
	/**
	 * 是否在线
	 */
	private boolean online;

	static UserServerState onlineData(long playerId){
		UserServerState data = new UserServerState();
		data.groupId = ServerNodeManager.getCurrServerInfo().getServerGroupId();
		data.serverId = ServerNodeManager.getCurrServerId();
		data.redisKey = redisKey(playerId);
		data.playerId = playerId;
		data.online = true;
		return data;
	}

	public String getRedisKey() {
		return redisKey;
	}

	public long getPlayerId() {
		return playerId;
	}

	/**
	 * 返回redis key
	 * @param playerId 玩家id
	 * @return redis key
	 */
	public static String redisKey(long playerId) {
		return REDIS_PREFIX_KEY + playerId;
	}

	public int getGroupId() {
		return groupId;
	}

	public int getServerId() {
		return serverId;
	}

	public boolean isOnline() {
		return online;
	}
}
