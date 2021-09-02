package org.qiunet.game.tests.protocol.proto.login;

import com.baidu.bjf.remoting.protobuf.annotation.Protobuf;
import org.qiunet.flash.handler.context.request.data.pb.IpbChannelData;
import org.qiunet.flash.handler.context.request.data.pb.PbChannelData;
import org.qiunet.game.tests.protocol.ProtocolId;

/***
 * 注册协议.
 *
 * qiunet
 * 2021/8/1 21:41
 **/
@PbChannelData(ID = ProtocolId.Login.REGISTER_RSP, desc = "注册协议响应")
public class RegisterResponse implements IpbChannelData {
	@Protobuf(description = "注册后的角色数据")
	private LoginInfo loginInfo;

	public static RegisterResponse valueOf(LoginInfo loginInfo) {
		RegisterResponse req = new RegisterResponse();
		req.loginInfo = loginInfo;
		return req;

	}

	public LoginInfo getLoginInfo() {
		return loginInfo;
	}

	public void setLoginInfo(LoginInfo loginInfo) {
		this.loginInfo = loginInfo;
	}
}
