package org.qiunet.test.handler.proto;

import com.baidu.bjf.remoting.protobuf.annotation.Protobuf;
import org.qiunet.flash.handler.context.request.data.ChannelData;
import org.qiunet.flash.handler.context.request.data.IChannelData;

/***
 *
 *
 * @author qiunet
 * 2020-09-22 12:30
 */
@ChannelData(ID = ProtocolId.Test.TCP_PB_LOGIN_REQ, desc = "tcp protobuf 测试")
public class TcpPbLoginRequest implements IChannelData {

	private String account;

	private String secret;

	private int phoneNum;
	@Protobuf(description = "性别")
	private GenderType gender;

	public TcpPbLoginRequest() {
	}

	public static TcpPbLoginRequest valueOf(String account, String secret, int phoneNum, GenderType gender) {
		TcpPbLoginRequest request = new TcpPbLoginRequest();
		request.account = account;
		request.secret = secret;
		request.phoneNum = phoneNum;
		request.gender = gender;
		return request;
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

	public void setAccount(String account) {
		this.account = account;
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
