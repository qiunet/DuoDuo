package org.qiunet.tests.proto;

import org.qiunet.flash.handler.context.request.data.pb.IpbChannelData;
import org.qiunet.flash.handler.context.request.data.pb.PbChannelData;

import static org.qiunet.tests.protocol.ProtocolId.Test.HTTP_LOGIN_RESP;

/***
 *
 *
 * @author qiunet
 * 2020-09-22 20:45
 */
@PbChannelData(ID = HTTP_LOGIN_RESP, desc = "登录响应")
public class LoginResponse implements IpbChannelData {

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
