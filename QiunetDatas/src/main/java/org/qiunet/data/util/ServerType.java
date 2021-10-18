package org.qiunet.data.util;

import org.qiunet.utils.exceptions.EnumParseException;

/***
 * 服务类型
 *
 * 建议ALL LOGIC 的serverId 可以1 - 99999
 * 其它按照serverType * 100000 + 自增
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
	/**
	 *  路由服务
	 */
	ROUTE(4),
	;
	private final int type;

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
