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
		int LOGIN_REQ = 1001;
		int LOGIN_RSP = 1001001;

		// 注册角色
		int REGISTER_REQ = 1002;
		int REGISTER_RSP = 1002001;

		// 获取随机昵称
		int RANDOM_NAME_REQ = 1003;
		int RANDOM_NAME_RSP = 1003001;

		// 使用指定角色进入游戏.
		int PLAYER_INDEX_REQ = 1004;
		int PLAYER_INDEX_RSP = 1004001;

	}

	interface Player {
		// 经验变动推送
		int EXP_CHANGE_PUSH = 2000001;

		// 获得经验. 比如打怪. 等
		int GET_EXP_REQ = 2001;
		int GET_EXP_RSP = 2001001;

		// 升级
		int UPGRADE_LV_REQ = 2002;
		int UPGRADE_LV_RSP = 2002001;
	}
}
