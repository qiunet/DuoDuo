package org.qiunet.game.tests.protocol.proto;

import com.baidu.bjf.remoting.protobuf.annotation.Protobuf;
import org.qiunet.flash.handler.context.request.data.pb.IpbChannelData;
import org.qiunet.flash.handler.context.request.data.pb.PbChannelData;
import org.qiunet.game.tests.protocol.ProtocolId;

/***
 * 获取随机名称响应
 *
 * qiunet
 * 2021/8/4 11:07
 **/
@PbChannelData(ID = ProtocolId.Login.RANDOM_NAME_RSP, desc = "获取随机名称响应")
public class RandomNameResponse implements IpbChannelData {
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
