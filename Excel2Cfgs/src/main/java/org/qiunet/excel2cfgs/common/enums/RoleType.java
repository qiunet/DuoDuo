package org.qiunet.excel2cfgs.common.enums;

/***
 * 角色类型
 *
 * @author qiunet
 * 2020-01-10 12:20
 ***/
public enum  RoleType {
	/***
	 *服务端
	 */
	SERVER("服务端"),
	/**
	 *客户端
	 */
	CLENTER("客户端"),
	/**
	 * 策划
	 */
	SCHEMER("策划"),
	;
	private final String name;

	RoleType(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return name;
	}
}
