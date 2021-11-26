package org.qiunet.test.handler.proto;

import com.baidu.bjf.remoting.protobuf.annotation.Protobuf;
import org.qiunet.flash.handler.context.request.data.ChannelData;
import org.qiunet.flash.handler.context.request.data.IChannelData;

import static org.qiunet.test.handler.proto.ProtocolId.Test.WS_PB_LOGIN_REQ;

/***
 *
 *
 * @author qiunet
 * 2020-09-22 12:30
 */
@ChannelData(ID = WS_PB_LOGIN_REQ, desc = "protobuf 测试")
public class WsPbLoginRequest implements IChannelData {

	private String account;

	private String secret;

	private int phoneNum;
	@Protobuf(description = "性别")
	private GenderType gender;

	public WsPbLoginRequest() {
	}

	public static WsPbLoginRequest valueOf(String account, String secret, int phoneNum) {
		WsPbLoginRequest request = new WsPbLoginRequest();
		request.account = account;
		request.secret = secret;
		request.phoneNum = phoneNum;
		return request;
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
