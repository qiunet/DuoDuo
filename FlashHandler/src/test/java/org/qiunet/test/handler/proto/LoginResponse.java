package org.qiunet.test.handler.proto;

import org.qiunet.flash.handler.context.request.data.ChannelData;
import org.qiunet.flash.handler.context.request.data.IChannelData;

/***
 *
 *
 * @author qiunet
 * 2020-09-22 12:31
 */
@ChannelData(ID = ProtocolId.Test.LOGIN_RESP, desc = "登录下行")
public class LoginResponse implements IChannelData {

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
