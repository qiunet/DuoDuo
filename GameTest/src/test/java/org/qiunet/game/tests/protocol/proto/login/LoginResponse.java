package org.qiunet.game.tests.protocol.proto.login;

import com.baidu.bjf.remoting.protobuf.annotation.Protobuf;
import org.qiunet.flash.handler.context.request.data.ChannelData;
import org.qiunet.flash.handler.context.request.data.IChannelData;
import org.qiunet.game.tests.protocol.ProtocolId;

import java.util.List;

/***
 * 登录下行
 *
 * @author qiunet
 * 2020-09-22 20:45
 */
@ChannelData(ID = ProtocolId.Login.LOGIN_RSP, desc = "登录响应")
public class LoginResponse extends IChannelData {
	@Protobuf(description = "账号下的所有角色")
	private List<LoginInfo> infos;

	public static LoginResponse valueOf(List<LoginInfo> infos) {
		LoginResponse response = new LoginResponse();
		response.infos = infos;
		return response;
	}

	public List<LoginInfo> getInfos() {
		return infos;
	}

	public void setInfos(List<LoginInfo> infos) {
		this.infos = infos;
	}
}
