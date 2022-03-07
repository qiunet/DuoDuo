package org.qiunet.function.gm.proto.rsp;

import com.baidu.bjf.remoting.protobuf.annotation.Protobuf;

/***
 *
 * @author qiunet
 * 2022/3/7 11:58
 */

public class OnlineUserInfo {
	@Protobuf(description = "玩家openId")
	private String openId;
	@Protobuf(description = "玩家ID")
	private long playerId;

	public static OnlineUserInfo valueOf(String openId, long playerId) {
		OnlineUserInfo info = new OnlineUserInfo();
		info.openId = openId;
		info.playerId = playerId;
		return info;
	}

	public String getOpenId() {
		return openId;
	}

	public void setOpenId(String openId) {
		this.openId = openId;
	}

	public long getPlayerId() {
		return playerId;
	}

	public void setPlayerId(long playerId) {
		this.playerId = playerId;
	}
}
