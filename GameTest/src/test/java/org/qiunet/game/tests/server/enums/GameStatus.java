package org.qiunet.game.tests.server.enums;

import org.qiunet.flash.handler.context.status.IGameStatus;

/***
 *
 *
 * qiunet
 * 2021/8/20 12:03
 **/
public enum GameStatus implements IGameStatus {

	RANDOM_NAME_POOL_EMPTY(100101, "随机名称池为空"),

	RANDOM_NAME_ALREADY_USED(100102, "随机名称已经被用"),

	REGISTER_COUNT_MAX(100201, "注册人数已经最大"),
	;
	private final int status;
	private final String desc;

	GameStatus(int status, String desc) {
		this.status = status;
		this.desc = desc;
	}

	@Override
	public int getStatus() {
		return status;
	}

	@Override
	public String getDesc() {
		return desc;
	}
}
