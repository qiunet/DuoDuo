package org.qiunet.test.handler.proto;

import com.baidu.bjf.remoting.protobuf.annotation.Protobuf;
import org.qiunet.flash.handler.context.request.data.ChannelData;
import org.qiunet.flash.handler.context.request.data.IChannelData;

import static org.qiunet.test.handler.proto.ProtocolId.Test.HTTP_PB_LOGIN_REQ;

/***
 *
 *
 * @author qiunet
 * 2020-09-22 12:30
 */
@ChannelData(ID = HTTP_PB_LOGIN_REQ, desc = "login http protobuf type")
public class HttpPbLoginRequest extends IChannelData {

	private String account;

	private String secret;

	private int phoneNum;
	@Protobuf(description = "性别")
	private GenderType gender;

	public HttpPbLoginRequest() {
	}

	public static HttpPbLoginRequest valueOf(String account, String secret, int phoneNum) {
		HttpPbLoginRequest request = new HttpPbLoginRequest();
		request.account = account;
		request.secret = secret;
		request.phoneNum = phoneNum;
		return request;
	}


	public void setAccount(String account) {
		this.account = account;
	}

	public void setSecret(String secret) {
		this.secret = secret;
	}

	public void setPhoneNum(int phoneNum) {
		this.phoneNum = phoneNum;
	}

	public void setGender(GenderType gender) {
		this.gender = gender;
	}

	public GenderType getGender() {
		return gender;
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
