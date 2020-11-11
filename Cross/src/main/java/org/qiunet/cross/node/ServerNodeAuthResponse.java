package org.qiunet.cross.node;

import com.baidu.bjf.remoting.protobuf.annotation.Protobuf;
import com.baidu.bjf.remoting.protobuf.annotation.ProtobufClass;
import org.qiunet.flash.handler.context.request.data.pb.IpbRequestData;
import org.qiunet.flash.handler.util.SkipProtoGenerator;

/***
 *
 *
 * @author qiunet
 * 2020-11-06 11:42
 */
@SkipProtoGenerator
@ProtobufClass(description = "鉴权后的响应. 是成功失败.")
public class ServerNodeAuthResponse implements IpbRequestData {
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
