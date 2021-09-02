package org.qiunet.game.tests.protocol.proto.login;

import com.baidu.bjf.remoting.protobuf.annotation.Ignore;
import com.baidu.bjf.remoting.protobuf.annotation.Protobuf;
import org.qiunet.game.tests.protocol.proto.player.ExpChangePush;
import org.qiunet.game.tests.server.context.PlayerActor;

/***
 * 玩家的数据
 *
 * qiunet
 * 2021/9/2 10:50
 **/
public class PlayerData {
	/**
	 * 为了测试方便. 默认升级经验100 . 而且不限制最高等级
	 */
	public static final int DEFAULT_UPGRADE_EXP = 100;

	@Ignore
	private transient PlayerActor playerActor;

	@Protobuf(description = "玩家的id")
	private long playerId;
	@Protobuf(description = "玩家的经验")
	private long exp;
	@Protobuf(description = "玩家的等级")
	private int lv;



	public static PlayerData valueOf(PlayerActor playerActor) {
		PlayerData data = valueOf(playerActor.getId(), 0, 1);
		data.playerActor = playerActor;
		return data;
	}

	public static PlayerData valueOf(long playerId, long exp, int lv) {
		PlayerData data = new PlayerData();
		data.playerId = playerId;
		data.exp = exp;
		data.lv = lv;
		return data;
	}

	public long getPlayerId() {
		return playerId;
	}

	public long getExp() {
		return exp;
	}

	public void setExp(long exp) {
		this.exp = exp;
	}

	public int getLv() {
		return lv;
	}

	public void setLv(int lv) {
		this.lv = lv;
	}

	/**
	 * 服务器端加经验.
	 * @param addExp
	 */
	public void addExp(int addExp) {
		this.exp += addExp;
		playerActor.sendMessage(ExpChangePush.valueOf(this.lv, this.exp, addExp));
	}

	/**
	 * 升级
	 * @return 升级的等级
	 */
	public int upgrade() {
		int olv = lv;
		while (exp >= DEFAULT_UPGRADE_EXP) {
			exp -= DEFAULT_UPGRADE_EXP;
			lv ++;
		}


		int diffLv = lv - olv;
		if (diffLv > 0) {
			playerActor.sendMessage(ExpChangePush.valueOf(this.lv, this.exp, 0));
		}
		return diffLv;
	}
}
