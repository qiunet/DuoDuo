package org.qiunet.flash.handler.netty.server.kcp.shakehands.message;

import com.baidu.bjf.remoting.protobuf.annotation.Protobuf;
import org.qiunet.flash.handler.common.id.IProtocolId;
import org.qiunet.flash.handler.context.request.data.ChannelData;
import org.qiunet.flash.handler.context.request.data.IChannelData;

/***
 *
 * @author qiunet
 * 2022/5/10 14:39
 */
@ChannelData(ID = IProtocolId.System.KCP_CONNECT_RSP, desc = "kcp连接响应")
public class KcpConnectRsp implements IChannelData {
	/**
	 * 回话ID可以从session获取.
	 * 这里按理可以不传
	 */
	@Protobuf(description = "回话ID")
	private int convId;

	public static KcpConnectRsp valueOf(int convId) {
		KcpConnectRsp data = new KcpConnectRsp();
		data.convId = convId;
		return data;
	}

	public int getConvId() {
		return convId;
	}

	public void setConvId(int convId) {
		this.convId = convId;
	}
}
