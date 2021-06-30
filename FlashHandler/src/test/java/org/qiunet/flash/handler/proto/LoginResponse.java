package org.qiunet.flash.handler.proto;

import com.baidu.bjf.remoting.protobuf.annotation.ProtobufClass;
import org.qiunet.flash.handler.context.request.data.pb.IpbResponseData;
import org.qiunet.flash.handler.context.request.data.pb.PbChannelDataID;

/***
 *
 *
 * @author qiunet
 * 2020-09-22 12:31
 */
@PbChannelDataID(1000001)
@ProtobufClass(description = "登录下行")
public class LoginResponse implements IpbResponseData {

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
