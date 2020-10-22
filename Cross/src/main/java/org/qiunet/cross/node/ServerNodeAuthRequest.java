package org.qiunet.cross.node;

import com.baidu.bjf.remoting.protobuf.annotation.Protobuf;
import com.baidu.bjf.remoting.protobuf.annotation.ProtobufClass;
import org.qiunet.flash.handler.context.request.data.pb.IpbRequestData;

/***
 *
 * serverNode  鉴权请求
 *
 * @author qiunet
 * 2020-10-22 15:57
 */
@ProtobufClass
public class ServerNodeAuthRequest implements IpbRequestData {
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
