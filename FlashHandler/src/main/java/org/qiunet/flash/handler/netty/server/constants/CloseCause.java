package org.qiunet.flash.handler.netty.server.constants;

/***
 * 关闭session的原因
 *
 * @author qiunet
 * 2020-03-02 14:58
 ***/
public enum CloseCause {
	/**
	 * 通道关闭
	 */
	CHANNEL_CLOSE("通道关闭"),
	/***
	 * 老session还处于激活状态 关闭老session
	 */
	LOGIN_REPEATED("老session还处于激活状态 关闭老session"),
	/**
	 * 通道空闲太久
	 */
	CHANNEL_IDLE("通道空闲太久"),
	/***
	 * 请求过快
	 */
	FAST_REQUEST("请求过快"),
	/**
	 * 网络错误
	 */
	NET_ERROR("网络错误"),
	/**
	 * 未鉴权的非法请求
	 */
	ERR_REQUEST("未鉴权的非法请求"),
	/**
	 * serverHandler 层异常
	 */
	EXCEPTION("serverHandler 层异常"),
	/**
	 * 登录信息丢失
	 */
	AUTH_LOST("登录信息丢失"),
	/**
	 * IP 封禁
	 */
	FORBID_IP("IP 封禁"),
	/***
	 * 账号被封禁
	 */
	FORBID_ACCOUNT("账号被封禁"),
	/**
	 * 退出登录
	 */
	LOGOUT("退出登录"),
	;
	private String desc;

	CloseCause(String desc) {
		this.desc = "[" + name() + "]" + "(" + desc + ")";
	}

	public String getDesc() {
		return desc;
	}
}
