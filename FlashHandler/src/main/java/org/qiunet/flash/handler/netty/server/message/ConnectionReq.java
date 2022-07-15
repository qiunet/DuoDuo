package org.qiunet.flash.handler.netty.server.message;

import com.baidu.bjf.remoting.protobuf.annotation.Protobuf;
import org.qiunet.flash.handler.common.id.IProtocolId;
import org.qiunet.flash.handler.context.request.data.ChannelData;
import org.qiunet.flash.handler.context.request.data.IChannelData;

/***
 *
 * Tcp Ws Udp 连接都需要先ConnectionReq.
 * 但是udp 如果是depend On Tcp 就不需要.
 * 因为调用 KcpBindAuthReq 绑定的tcp playerActor 已经有了.
 *
 * @author qiunet
 * 2022/7/15 17:01
 */
@ChannelData(ID = IProtocolId.System.CONNECTION_REQ, desc = "idKey设置请求")
public class ConnectionReq implements IChannelData {
	@Protobuf(description = "openId或者其它")
	private String idKey;

	public static ConnectionReq valueOf(String idKey) {
		ConnectionReq data = new ConnectionReq();
		data.idKey = idKey;
		return data;
	}

	public String getIdKey() {
		return idKey;
	}

	public void setIdKey(String idKey) {
		this.idKey = idKey;
	}
}
