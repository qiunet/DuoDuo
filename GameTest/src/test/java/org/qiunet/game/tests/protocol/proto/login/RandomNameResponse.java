package org.qiunet.game.tests.protocol.proto.login;

import com.baidu.bjf.remoting.protobuf.annotation.Protobuf;
import org.qiunet.flash.handler.context.request.data.ChannelData;
import org.qiunet.flash.handler.context.request.data.IChannelData;
import org.qiunet.game.tests.protocol.ProtocolId;

/***
 * 获取随机名称响应
 *
 * qiunet
 * 2021/8/4 11:07
 **/
@ChannelData(ID = ProtocolId.Login.RANDOM_NAME_RSP, desc = "获取随机名称响应")
public class RandomNameResponse implements IChannelData {
	@Protobuf(description = "随机的昵称")
	private String nick;

	public static RandomNameResponse valueOf(String nick) {
		RandomNameResponse rsp = new RandomNameResponse();
		rsp.nick = nick;
		return rsp;
	}

	public String getNick() {
		return nick;
	}

	public void setNick(String nick) {
		this.nick = nick;
	}
}
