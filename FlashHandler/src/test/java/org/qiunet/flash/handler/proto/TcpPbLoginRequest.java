package org.qiunet.flash.handler.proto;

import com.baidu.bjf.remoting.protobuf.annotation.Protobuf;
import com.baidu.bjf.remoting.protobuf.annotation.ProtobufClass;
import org.qiunet.flash.handler.context.request.data.pb.IpbRequestData;

/***
 *
 *
 * @author qiunet
 * 2020-09-22 12:30
 */
@ProtobufClass(description = "登录协议")
public class TcpPbLoginRequest implements IpbRequestData {

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