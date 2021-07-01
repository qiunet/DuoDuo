package org.qiunet.cross.node;

import com.baidu.bjf.remoting.protobuf.annotation.Protobuf;
import org.qiunet.flash.handler.common.id.IProtocolId;
import org.qiunet.flash.handler.context.request.data.pb.IpbChannelData;
import org.qiunet.flash.handler.context.request.data.pb.PbChannelData;
import org.qiunet.flash.handler.util.SkipProtoGenerator;

/***
 *
 *
 * @author qiunet
 * 2020-11-06 11:42
 */
@SkipProtoGenerator
@PbChannelData(ID = IProtocolId.System.SERVER_NODE_AUTH_RESP, desc = "serverNode 鉴权请求响应")
public class ServerNodeAuthResponse implements IpbChannelData {
	@Protobuf(description = "鉴权结果")
	private boolean result;

	public static ServerNodeAuthResponse valueOf(boolean result) {
		ServerNodeAuthResponse response = new ServerNodeAuthResponse();
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
