package org.qiunet.flash.handler.netty.server.constants;

/***
 * 关闭session的原因
 *
 * @author qiunet
 * 2020-03-02 14:58
 ***/
public enum  CloseCause {
	/**
	 * 通道关闭
	 */
	CHANNEL_CLOSE,
	/***
	 * 老session还处于激活状态 关闭老session
	 */
	LOGIN_REPEATED,
	/**
	 * 通道空闲太久
	 */
	CHANNEL_IDLE,
	/***
	 * 被封禁
	 */
	FORBID_ROLE,
	/***
	 * 请求过快
	 */
	FAST_REQUEST,
	/**
	 * 网络错误
	 */
	NET_ERROR,
	;
}
