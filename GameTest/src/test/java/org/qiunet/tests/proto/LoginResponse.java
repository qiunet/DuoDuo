package org.qiunet.tests.proto;

import org.qiunet.flash.handler.context.request.data.pb.IpbChannelData;
import org.qiunet.flash.handler.context.request.data.pb.PbChannelDataID;

/***
 *
 *
 * @author qiunet
 * 2020-09-22 20:45
 */
@PbChannelDataID(ID = 2000, desc = "登录响应")
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
