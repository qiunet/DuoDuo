package org.qiunet.cross.node;

import com.baidu.bjf.remoting.protobuf.annotation.Protobuf;
import org.qiunet.flash.handler.common.id.IProtocolId;
import org.qiunet.flash.handler.context.request.data.ChannelData;
import org.qiunet.flash.handler.context.request.data.IChannelData;
import org.qiunet.flash.handler.context.request.data.ServerCommunicationData;
import org.qiunet.flash.handler.util.proto.SkipProtoGenerator;

/***
 *
 *
 * @author qiunet
 * 2020-11-06 11:42
 */
@SkipProtoGenerator
@ServerCommunicationData
@ChannelData(ID = IProtocolId.System.SERVER_NODE_AUTH_RSP, desc = "serverNode 鉴权请求响应")
public class ServerNodeAuthRsp implements IChannelData {
	@Protobuf(description = "鉴权结果")
	private boolean result;

	public static ServerNodeAuthRsp valueOf(boolean result) {
		ServerNodeAuthRsp response = new ServerNodeAuthRsp();
		response.result = result;
		return response;
	}

	public boolean isResult() {
		return result;
	}

	public void setResult(boolean result) {
		this.result = result;
	}
}
