package org.qiunet.flash.handler.proto;

import com.baidu.bjf.remoting.protobuf.annotation.ProtobufClass;
import org.qiunet.flash.handler.context.request.data.pb.IpbRequestData;

/***
 *
 *
 * @author qiunet
 * 2020-09-22 12:30
 */
@ProtobufClass
public class LoginRequest implements IpbRequestData {

	private String account;

	private String secret;

	private int phoneNum;

	public LoginRequest() {
	}

	public static LoginRequest valueOf(String account, String secret, int phoneNum) {
		LoginRequest request = new LoginRequest();
		request.account = account;
		request.secret = secret;
		request.phoneNum = phoneNum;
		return request;
	}

	public String getAccount() {
		return account;
	}

	public String getSecret() {
		return secret;
	}

	public int getPhoneNum() {
		return phoneNum;
	}
}
