package org.qiunet.flash.handler.common.player.offline.enums;

/***
 * 离线用户 销毁原因
 * @author qiunet
 * 2023/8/16 09:59
 */
public enum OfflinePlayerDestroyCause {
	/**
	 * 被登录用户踢出
	 */
	KICK_OUT,
	/**
	 * 服务原因
	 */
	SERVER_DEPRECATE,
	/**
	 * 服务原因
	 */
	SERVER_CLOSE,
	/**
	 * 服务原因
	 */
	SERVER_SHUTDOWN,
	/**
	 * 时间到了.
	 */
	TIMEOUT,
}
