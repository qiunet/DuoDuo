package org.qiunet.test.handler.proto;

import com.baidu.bjf.remoting.protobuf.annotation.Protobuf;
import com.baidu.bjf.remoting.protobuf.annotation.ProtobufClass;
import org.qiunet.flash.handler.util.NeedProtoGenerator;

/***
 *
 * @author qiunet
 * 2020-10-31 15:16
 **/
@NeedProtoGenerator
@ProtobufClass(description = "测试类")
public class TestNeedCreateFile {
	@Protobuf(description = "玩家id")
	private int playerId;
	@Protobuf(description = "玩家名")
	private String playerName;

	public int getPlayerId() {
		return playerId;
	}

	public void setPlayerId(int playerId) {
		this.playerId = playerId;
	}

	public String getPlayerName() {
		return playerName;
	}

	public void setPlayerName(String playerName) {
		this.playerName = playerName;
	}
}
