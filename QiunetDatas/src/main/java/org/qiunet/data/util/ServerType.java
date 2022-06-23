package org.qiunet.data.util;

import com.google.common.collect.Maps;
import org.qiunet.utils.exceptions.EnumParseException;

import java.util.Map;

/***
 * 服务类型
 *
 * 按照类型  组ID*1000 + 类型 * 100 + 自增ID
 *
 * 示例: 2103
 * * 服务组ID: 2
 * * 服务类型: 1
 * * 组内序号: 03
 *
 *  每个组100个服务器序号ID
 *  同一个组共用一个数据库
 *
 * @author qiunet
 * 2020-10-09 11:08
 */
public enum ServerType {
	/**
	 * 没有分配类型
	 */
	NONE(0),
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
	/**
	 * 后台
	 */
	BACKSTAGE(4),
	/**
	 * 中心服
	 */
	CENTER(5),
	/**
	 * 匹配服务器
	 */
	MATCH(6),

	;
	private final int type;

	private static final Map<Integer, Integer> groupIdLength = Maps.newConcurrentMap();
	/**
	 * 获得serverId 里面groupID 长度
	 * @param groupId
	 * @return
	 */
	public static int getGroupIdLength(int groupId) {
		if (groupId == 0) {
			return 0;
		}
		return groupIdLength.computeIfAbsent(groupId, id -> (int) (Math.log10(id) + 1));
	}

	/**
	 * 获得服务器对应的组ID
	 * @param serverId
	 * @return
	 */
	public static int getGroupId(int serverId) {
		return serverId / 1000;
	}

	/**
	 * 获得serverType
	 * @param serverId
	 * @return
	 */
	public static ServerType getServerType(int serverId) {
		int type = (serverId % 1000) / 100;
		return parse(type);
	}

	/**
	 * 构造一个serverId
	 * @param groupId
	 * @param incrId
	 * @return
	 */
	public int buildServerId(int groupId, int incrId) {
		return groupId * 1000 + getType() * 100 + incrId;
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
