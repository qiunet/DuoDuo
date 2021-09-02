package org.qiunet.game.tests.protocol.proto.login;

import com.baidu.bjf.remoting.protobuf.annotation.Protobuf;
import org.qiunet.flash.handler.context.request.data.pb.IpbChannelData;
import org.qiunet.flash.handler.context.request.data.pb.PbChannelData;
import org.qiunet.game.tests.protocol.ProtocolId;
import org.qiunet.game.tests.protocol.enums.GenderType;

/***
 *
 * qiunet
 * 2021/8/1 21:41
 **/
@PbChannelData(ID = ProtocolId.Login.REGISTER_REQ, desc = "注册协议请求")
public class RegisterRequest implements IpbChannelData {
	@Protobuf(description = "昵称")
	private String nick;
	@Protobuf(description = "性别")
	private GenderType gender;
	@Protobuf(description = "头像")
	private int img;

	public static RegisterRequest valueOf(String nick, GenderType gender, int img) {
		RegisterRequest req = new RegisterRequest();
		req.gender = gender;
		req.nick = nick;
		req.img = img;
		return req;

	}

	public int getImg() {
		return img;
	}

	public void setImg(int img) {
		this.img = img;
	}

	public String getNick() {
		return nick;
	}

	public void setNick(String nick) {
		this.nick = nick;
	}

	public GenderType getGender() {
		return gender;
	}

	public void setGender(GenderType gender) {
		this.gender = gender;
	}
}
