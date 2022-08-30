package org.qiunet.game.tests.protocol.proto.login;

import com.baidu.bjf.remoting.protobuf.annotation.Protobuf;
import org.qiunet.flash.handler.context.request.data.ChannelData;
import org.qiunet.flash.handler.context.request.data.IChannelData;

import static org.qiunet.game.tests.protocol.ProtocolId.Login.PLAYER_INDEX_REQ;


/***
 *  指定角色ID 进入游戏.
 *
 * @author qiunet
 * 2020-09-23 10:18
 */
@ChannelData(ID = PLAYER_INDEX_REQ, desc = "长连接首页")
public class PlayerIndexRequest extends IChannelData {
	@Protobuf(description = "玩家id")
	private long playerId;

	public static PlayerIndexRequest valueOf(long playerId) {
		PlayerIndexRequest req = new PlayerIndexRequest();
		req.playerId = playerId;
		return req;
	}

	public long getPlayerId() {
		return playerId;
	}

	public void setPlayerId(long playerId) {
		this.playerId = playerId;
	}
}
