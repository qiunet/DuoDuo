package org.qiunet.game.tests.protocol.proto.player;

import com.baidu.bjf.remoting.protobuf.annotation.Protobuf;
import org.qiunet.flash.handler.context.request.data.ChannelData;
import org.qiunet.flash.handler.context.request.data.IChannelData;
import org.qiunet.game.tests.protocol.ProtocolId;

/***
 *
 *
 * qiunet
 * 2021/9/2 10:09
 **/
@ChannelData(ID = ProtocolId.Player.EXP_CHANGE_PUSH, desc = "获得经验推送")
public class ExpChangePush implements IChannelData {
	@Protobuf(description = "当前等级")
	private int level;
	@Protobuf(description = "当前经验")
	private long exp;
	@Protobuf(description = "变动的经验")
	private long addExp;

	public static ExpChangePush valueOf(int level, long exp, long addExp){
		ExpChangePush push = new ExpChangePush();
		push.addExp =addExp;
		push.level =level;
		push.exp =exp;
		return push;
	}

	public int getLevel() {
		return level;
	}

	public long getExp() {
		return exp;
	}

	public long getAddExp() {
		return addExp;
	}
}
