package org.qiunet.test.handler.proto;

import org.qiunet.flash.handler.common.id.IProtocolId;

/***
 *
 * qiunet
 * 2021/6/30 21:49
 **/
public interface ProtocolId extends IProtocolId {

	interface Test {
		int WS_PB_LOGIN_REQ = 1001;
		int HTTP_PB_LOGIN_REQ = 2001;
		int TCP_PB_LOGIN_REQ = 3001;


		int LOGIN_RESP = 1001001;
	}

	interface Player {
		int PLAYER_LOGIN = 1002;

		int CROSS_PLAYER_LOGIN_SUCCESS = 1002001;
	}

	interface Equip {
		int EQUIP_INDEX = 1100;
	}

	interface Pool {
		int TEST_REQ = 1201;
		int TEST_RSP = 1201001;
	}
}
