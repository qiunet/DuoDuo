package org.qiunet.flash.handler.netty.server.constants;

import com.baidu.bjf.remoting.protobuf.annotation.Protobuf;
import com.baidu.bjf.remoting.protobuf.annotation.ProtobufClass;
import org.qiunet.flash.handler.util.proto.CommonModuleProto;

/***
 * 关闭session的原因
 *
 * @author qiunet
 * 2020-03-02 14:58
 ***/
@CommonModuleProto
@ProtobufClass(description = "连接关闭的原因")
public enum CloseCause {
	@Protobuf(description = "通道关闭")
	CHANNEL_CLOSE("通道关闭", true),

	@Protobuf(description = "通道关闭")
	CONNECTION_ID_KEY_ERROR("Connection的id key错误", false),
	@Protobuf(description = "无效断线重连")
	RECONNECT_INVALID("无效断线重连", false),
	@Protobuf(description = "老session还处于激活状态 关闭老session")
	LOGIN_REPEATED("老session还处于激活状态 关闭老session", false),
	@Protobuf(description = "通道空闲太久")
	CHANNEL_IDLE("通道空闲太久", true),
	@Protobuf(description = "请求过快")
	FAST_REQUEST("请求过快", true),
	@Protobuf(description = "网络错误")
	NET_ERROR("网络错误", true),
	@Protobuf(description = "未鉴权的非法请求")
	ERR_REQUEST("未鉴权的非法请求", false),
	@Protobuf(description = "serverHandler 层异常")
	EXCEPTION("serverHandler 层异常", true),
	@Protobuf(description = "登录信息丢失")
	AUTH_LOST("登录信息丢失", false),
	@Protobuf(description = "IP 封禁")
	FORBID_IP("IP 封禁", false),
	@Protobuf(description = "账号被封禁")
	FORBID_ACCOUNT("账号被封禁", false),
	@Protobuf(description = "退出登录")
	LOGOUT("退出登录", false),
	@Protobuf(description = "服务关闭")
	SERVER_SHUTDOWN("服务关闭", false),
	@Protobuf(description = "不活跃了.")
	INACTIVE("不活跃了", true),
	@Protobuf(description = "销毁")
	DESTROY("销毁", false),
	;
	private final boolean waitConnect;
	private final boolean logoutPush;
	private final String desc;

	CloseCause(String desc, boolean waitConnect) {
		this(desc, waitConnect, true);
	}
	CloseCause(String desc, boolean waitConnect, boolean logoutPush) {
		this.desc = "[" + name() + "]" + "(" + desc + ")";
		this.waitConnect = waitConnect;
		this.logoutPush = logoutPush;
	}

	public boolean needLogoutPush() {
		return logoutPush;
	}

	public boolean needWaitConnect() {
		return waitConnect;
	}

	public String getDesc() {
		return desc;
	}
}
