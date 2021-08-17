package org.qiunet.game.tests.protocol.proto;

import com.baidu.bjf.remoting.protobuf.annotation.Protobuf;
import org.qiunet.game.tests.protocol.enums.GenderType;

/***
 * 玩家登录信息
 *
 * qiunet
 * 2021/8/4 10:14
 **/
public class LoginInfo {
	@Protobuf(description = "玩家id")
	private long playerId;
	@Protobuf(description = "昵称")
	private String nick;
	@Protobuf(description = "等级")
	private int level;
	@Protobuf(description = "性别")
	private GenderType gender;
	@Protobuf(description = "头像信息")
	private int img;

	public static LoginInfo valueOf(long playerId, GenderType gender, String nick, int level, int img) {
		LoginInfo info = new LoginInfo();
		info.playerId = playerId;
		info.gender = gender;
		info.level = level;
		info.nick = nick;
		info.img = img;
		return info;
	}

	public GenderType getGender() {
		return gender;
	}

	public void setGender(GenderType gender) {
		this.gender = gender;
	}

	public long getPlayerId() {
		return playerId;
	}

	public void setPlayerId(long playerId) {
		this.playerId = playerId;
	}

	public String getNick() {
		return nick;
	}

	public void setNick(String nick) {
		this.nick = nick;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public int getImg() {
		return img;
	}

	public void setImg(int img) {
		this.img = img;
	}
}
