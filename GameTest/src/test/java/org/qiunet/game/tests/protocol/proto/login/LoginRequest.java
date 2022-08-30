package org.qiunet.game.tests.protocol.proto.login;

import org.qiunet.flash.handler.context.request.data.ChannelData;
import org.qiunet.flash.handler.context.request.data.IChannelData;

import static org.qiunet.game.tests.protocol.ProtocolId.Login.LOGIN_REQ;


/***
 * 登录协议.
 *
 * @author qiunet
 * 2020-09-22 20:28
 */
@ChannelData(ID = LOGIN_REQ, desc = "登录")
public class LoginRequest extends IChannelData {

	private String openId;

	public static LoginRequest valueOf(String openId) {
		LoginRequest request = new LoginRequest();
		request.openId = openId;
		return request;
	}

	public String getOpenId() {
		return openId;
	}
}
