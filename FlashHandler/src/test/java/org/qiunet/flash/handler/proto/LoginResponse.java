package org.qiunet.flash.handler.proto;

import org.qiunet.flash.handler.context.request.data.pb.IpbChannelData;
import org.qiunet.flash.handler.context.request.data.pb.PbChannelData;

/***
 *
 *
 * @author qiunet
 * 2020-09-22 12:31
 */
@PbChannelData(ID = 1000001, desc = "登录下行")
public class LoginResponse implements IpbChannelData {

	private String testString;

	private GenderType gender;

	public static LoginResponse valueOf(String testString) {
		LoginResponse response = new LoginResponse();
		response.testString = testString;
		return response;
	}

	public GenderType getGender() {
		return gender;
	}

	public String getTestString() {
		return testString;
	}
}
