package org.qiunet.tests.proto;

import com.baidu.bjf.remoting.protobuf.annotation.ProtobufClass;
import org.qiunet.flash.handler.context.request.data.pb.IpbResponseData;
import org.qiunet.flash.handler.context.request.data.pb.PbResponse;

/***
 *
 *
 * @author qiunet
 * 2020-09-22 20:45
 */
@PbResponse(value = 2000)
@ProtobufClass(description = "登录响应")
public class LoginResponse implements IpbResponseData {

	private int uid;
	private String token;
	private boolean registered;

	public static LoginResponse valueOf(int uid, String token, boolean registered) {
		LoginResponse response = new LoginResponse();
		response.uid = uid;
		response.token = token;
		response.registered = registered;
		return response;
	}

	public int getUid() {
		return uid;
	}

	public String getToken() {
		return token;
	}

	public boolean isRegistered() {
		return registered;
	}
}
