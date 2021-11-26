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
	CHANNEL_CLOSE("通道关闭", true),
	/***
	 * 老session还处于激活状态 关闭老session
	 */
	LOGIN_REPEATED("老session还处于激活状态 关闭老session", false),
	/**
	 * 通道空闲太久
	 */
	CHANNEL_IDLE("通道空闲太久", true),
	/***
	 * 请求过快
	 */
	FAST_REQUEST("请求过快", true),
	/**
	 * 网络错误
	 */
	NET_ERROR("网络错误", true),
	/**
	 * 未鉴权的非法请求
	 */
	ERR_REQUEST("未鉴权的非法请求", false),
	/**
	 * serverHandler 层异常
	 */
	EXCEPTION("serverHandler 层异常", true),
	/**
	 * 登录信息丢失
	 */
	AUTH_LOST("登录信息丢失", false),
	/**
	 * IP 封禁
	 */
	FORBID_IP("IP 封禁", false),
	/***
	 * 账号被封禁
	 */
	FORBID_ACCOUNT("账号被封禁", false),
	/**
	 * 退出登录
	 */
	LOGOUT("退出登录", false),
	/**
	 * 服务关闭
	 */
	SERVER_SHUTDOWN("服务关闭", false),
	/***
	 * 不活跃了.
	 */
	INACTIVE("不活跃了", true),
	/**
	 * 销毁
	 */
	DESTROY("销毁", false),
	;
	private final boolean waitConnect;
	private final String desc;

	CloseCause(String desc, boolean waitConnect) {
		this.desc = "[" + name() + "]" + "(" + desc + ")";
		this.waitConnect = waitConnect;
	}

	public boolean needWaitConnect() {
		return waitConnect;
	}

	public String getDesc() {
		return desc;
	}
}
