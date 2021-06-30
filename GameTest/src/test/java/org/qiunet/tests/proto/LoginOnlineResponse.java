package org.qiunet.tests.proto;

import org.qiunet.flash.handler.context.request.data.pb.IpbChannelData;
import org.qiunet.flash.handler.context.request.data.pb.PbChannelDataID;

/***
 *
 *
 * @author qiunet
 * 2020-09-22 20:56
 */
@PbChannelDataID(ID = 1000000,desc = "登录Online响应")
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
