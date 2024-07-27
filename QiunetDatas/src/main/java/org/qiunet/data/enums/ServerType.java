package org.qiunet.data.enums;

import org.qiunet.utils.exceptions.EnumParseException;

/***
 * 服务类型
 *
 * 按照类型  +  自增 * 10 + 类型ID
 *
 * 示例: 103
 * * 组内序号: 10
 * * 服务类型: 3
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
	/**
	 * 网关服务器
	 */
	GATE(7),
	;
	private final int type;


	/**
	 * 获得serverType
	 * @param serverId
	 * @return
	 */
	public static ServerType getServerType(int serverId) {
		int type = Math.abs(serverId) % 10;
		return parse(type);
	}

	/**
	 * 构造一个serverId
	 * @param incrId
	 * @return
	 */
	public int buildServerId(int incrId) {
		return incrId * 10 + getType();
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
