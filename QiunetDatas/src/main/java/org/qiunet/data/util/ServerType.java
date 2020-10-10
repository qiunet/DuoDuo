package org.qiunet.data.util;

import com.baidu.bjf.remoting.protobuf.EnumReadable;
import org.qiunet.utils.exceptions.EnumParseException;

/***
 * 服务类型
 *
 * @author qiunet
 * 2020-10-09 11:08
 */
public enum ServerType implements EnumReadable {
	/**
	 * 普通逻辑服务
	 */
	LOGIC(0),
	/**
	 * 跨服服务
	 */
	CROSS(1),
	/**
	 * 登录服务
	 */
	LOGIN(2),
	/**
	 *  路由服务
	 */
	ROUTE(3),
	/**
	 * 全功能服务
	 * 即服务包含所有的服务.
	 */
	ALL(100),
	;
	private int type;

	ServerType(int type) {
		this.type = type;
	}

	public int getType() {
		return type;
	}

	@Override
	public int value() {
		return type;
	}

	private static ServerType [] values = values();

	public static ServerType parse(int serverType) {
		if (serverType < 0 || serverType >= values.length) {
			throw new EnumParseException(serverType);
		}
		return values[serverType];
	}
}
