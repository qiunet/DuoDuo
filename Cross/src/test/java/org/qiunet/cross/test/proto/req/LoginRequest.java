package org.qiunet.cross.test.proto.req;

import com.baidu.bjf.remoting.protobuf.annotation.ProtobufClass;
import org.qiunet.flash.handler.context.request.data.pb.IpbRequestData;

/***
 *
 *
 * @author qiunet
 * 2020-10-23 09:55
 */
@ProtobufClass(description = "登录请求")
public class LoginRequest implements IpbRequestData {

	private long playerId;

	public static LoginRequest valueOf(long playerId) {
		LoginRequest request = new LoginRequest();
		request.playerId = playerId;
		return request;
	}

	public long getPlayerId() {
		return playerId;
	}

	public void setPlayerId(long playerId) {
		this.playerId = playerId;
	}
}
