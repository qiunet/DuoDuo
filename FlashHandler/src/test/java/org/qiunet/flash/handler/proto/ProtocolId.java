package org.qiunet.flash.handler.proto;

import org.qiunet.flash.handler.common.id.IProtocolId;

/***
 *
 *
 * qiunet
 * 2021/6/30 21:49
 **/
public interface ProtocolId extends IProtocolId {

	interface Test {
		int WS_PB_LOGIN_REQ = 1001;
		int HTTP_PB_LOGIN_REQ = 2001;

		int LOGIN_RESP = 1001001;
	}

	public interface Player {
		int PLAYER_LOGIN = 1002;

		int CROSS_PLAYER_LOGIN_SUCCESS = 1002001;
	}

	public interface Equip {
		int EQUIP_INDEX = 1100;
	}
}
