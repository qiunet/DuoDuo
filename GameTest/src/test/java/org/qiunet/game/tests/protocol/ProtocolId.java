package org.qiunet.game.tests.protocol;

import org.qiunet.flash.handler.common.id.IProtocolId;

/***
 *
 *
 * qiunet
 * 2021/6/30 21:29
 **/
public interface ProtocolId extends IProtocolId {

	interface Login {
		// 使用账号登入
		int LOGIN_REQ = 1000;
		int LOGIN_RSP = 1000001;

		// 注册角色
		int REGISTER_REQ = 1001;
		int REGISTER_RSP = 1001001;

		// 获取随机昵称
		int RANDOM_NAME_REQ = 1002;
		int RANDOM_NAME_RSP = 1002001;

		// 使用指定角色进入游戏.
		int PLAYER_INDEX_REQ = 1003;
		int PLAYER_INDEX_RSP = 1003001;

	}
}
