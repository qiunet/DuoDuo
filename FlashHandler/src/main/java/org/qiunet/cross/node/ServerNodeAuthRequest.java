package org.qiunet.cross.node;

import com.baidu.bjf.remoting.protobuf.annotation.Protobuf;
import org.qiunet.flash.handler.common.id.IProtocolId;
import org.qiunet.flash.handler.context.request.data.ChannelData;
import org.qiunet.flash.handler.context.request.data.IChannelData;
import org.qiunet.flash.handler.util.SkipProtoGenerator;

/***
 *
 * serverNode  鉴权请求
 *
 * @author qiunet
 * 2020-10-22 15:57
 */
@SkipProtoGenerator
@ChannelData(ID = IProtocolId.System.SERVER_NODE_AUTH, desc = "serverNode 鉴权请求")
public class ServerNodeAuthRequest implements IChannelData {
	@Protobuf(description = "请求serverId 鉴权")
	private int serverId;

	public static ServerNodeAuthRequest valueOf(int serverId) {
		ServerNodeAuthRequest request = new ServerNodeAuthRequest();
		request.serverId = serverId;
		return request;
	}

	public int getServerId() {
		return serverId;
	}

	public void setServerId(int serverId) {
		this.serverId = serverId;
	}
}
