package org.qiunet.game.tests.protocol.proto.login;

import com.baidu.bjf.remoting.protobuf.annotation.Protobuf;
import org.qiunet.flash.handler.context.request.data.ChannelData;
import org.qiunet.flash.handler.context.request.data.IChannelData;
import org.qiunet.game.tests.protocol.ProtocolId;

/***
 * 注册协议.
 *
 * qiunet
 * 2021/8/1 21:41
 **/
@ChannelData(ID = ProtocolId.Login.REGISTER_RSP, desc = "注册协议响应")
public class RegisterResponse extends IChannelData {
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
