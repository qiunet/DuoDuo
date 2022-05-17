package org.qiunet.flash.handler.netty.server.kcp.shakehands.message;

import com.baidu.bjf.remoting.protobuf.annotation.Protobuf;
import org.qiunet.flash.handler.common.id.IProtocolId;
import org.qiunet.flash.handler.context.request.data.ChannelData;
import org.qiunet.flash.handler.context.request.data.IChannelData;

/***
 *
 * @author qiunet
 * 2022/4/27 11:27
 */
@ChannelData(ID = IProtocolId.System.KCP_BIND_AUTH_RSP, desc = "KCP和其它长连接绑定鉴权响应", kcp = true)
public class KcpBindAuthRsp implements IChannelData {
	@Protobuf(description = "是否成功")
	private boolean success;

	public static KcpBindAuthRsp valueOf(boolean success) {
		KcpBindAuthRsp data = new KcpBindAuthRsp();
		data.success = success;
		return data;
	}

	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}
}
