package org.qiunet.tests.proto;

import org.qiunet.flash.handler.context.request.data.pb.IpbChannelData;
import org.qiunet.flash.handler.context.request.data.pb.PbChannelData;

import static org.qiunet.tests.protocol.ProtocolId.Test.LOGIN_REQ;

/***
 *
 *
 * @author qiunet
 * 2020-09-22 20:28
 */
@PbChannelData(ID = LOGIN_REQ, desc = "http登录")
public class LoginRequest implements IpbChannelData {

	private String openId;

	public static LoginRequest valueOf(String openId) {
		LoginRequest request = new LoginRequest();
		request.openId = openId;
		return request;
	}

	public String getOpenId() {
		return openId;
	}
}
