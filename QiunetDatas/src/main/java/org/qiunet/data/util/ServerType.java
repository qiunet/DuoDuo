package org.qiunet.data.util;

import com.google.common.collect.Maps;
import org.qiunet.utils.exceptions.EnumParseException;

import java.util.Map;

/***
 * 服务类型
 *
 * 按照类型 类型* 100000000 + 组ID * 1000 + 自增ID
 * 可以容纳100000个组  每个组1000个服务器ID
 * 一个组共用一个数据库
 *
 * @author qiunet
 * 2020-10-09 11:08
 */
public enum ServerType {
	/**
	 * 全功能服务
	 * 即服务包含所有的服务.
	 */
	ALL(0),
	/**
	 * 普通逻辑服务
	 */
	LOGIC(1),
	/**
	 * 跨服服务
	 */
	CROSS(2),
	/**
	 * 登录服务
	 */
	LOGIN(3),
	;
	private final int type;

	private static final Map<Integer, Integer> serverIdLength = Maps.newConcurrentMap();
	/**
	 * 获得serverId 长度
	 * @param serverId
	 * @return
	 */
	public static int getServerIdLength(int serverId) {
		return serverIdLength.computeIfAbsent(serverId, id -> (int) (Math.log10(id) + 1));
	}

	/**
	 * 获得服务器对应的组ID
	 * @param serverId
	 * @return
	 */
	public static int getGroupId(int serverId) {
		return (serverId % 100000000) / 1000;
	}

	/**
	 * 获得serverType
	 * @param serverId
	 * @return
	 */
	public static ServerType getServerType(int serverId) {
		int type = serverId / 100000000;
		return parse(type);
	}

	/**
	 * 构造一个serverId
	 * @param groupId
	 * @param incrId
	 * @return
	 */
	public int buildServerId(int groupId, int incrId) {
		return getType() * 100000000 + groupId * 1000 + incrId;
	}

	ServerType(int type) {
		this.type = type;
	}

	public int getType() {
		return type;
	}

	public int value() {
		return type;
	}

	private static final ServerType [] values = values();

	public static ServerType parse(int serverType) {
		if (serverType < 0 || serverType >= values.length) {
			throw new EnumParseException(serverType);
		}
		return values[serverType];
	}
}
