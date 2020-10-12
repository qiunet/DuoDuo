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
	 * 全功能服务
	 * 即服务包含所有的服务.
	 */
	ALL(0, true),
	/**
	 * 普通逻辑服务
	 */
	LOGIC(1, true),
	/**
	 * 跨服服务
	 */
	CROSS(2, false),
	/**
	 * 登录服务
	 */
	LOGIN(3, true),
	/**
	 *  路由服务
	 */
	ROUTE(4, true),
	;
	private int type;
	/**
	 * 服务到服务之间的session是否复用.
	 * 玩家去跨服的. 不需要复用.
	 * 服务去另一个服务取数据, 可以复用.
	 */
	private boolean sessionReUse;

	ServerType(int type, boolean sessionReUse) {
		this.sessionReUse = sessionReUse;
		this.type = type;
	}

	public boolean isSessionReUse() {
		return sessionReUse;
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
