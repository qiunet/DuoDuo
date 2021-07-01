package org.qiunet.tests.proto;

import org.qiunet.flash.handler.context.request.data.pb.IpbChannelData;
import org.qiunet.flash.handler.context.request.data.pb.PbChannelData;

import static org.qiunet.tests.protocol.ProtocolId.Test.ONLINE_LOGIN_RESP;

/***
 *
 *
 * @author qiunet
 * 2020-09-22 20:56
 */
@PbChannelData(ID = ONLINE_LOGIN_RESP, desc = "登录Online响应")
public class LoginOnlineResponse implements IpbChannelData {
	private int day;

	public static LoginOnlineResponse valueOf(int day) {
		LoginOnlineResponse response = new LoginOnlineResponse();
		response.day = day;
		return response;
	}

	public int getDay() {
		return day;
	}
}
