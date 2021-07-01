package org.qiunet.tests.protocol;

import org.qiunet.flash.handler.common.id.IProtocolId;

/***
 *
 *
 * qiunet
 * 2021/6/30 21:29
 **/
public interface ProtocolId extends IProtocolId {

	interface Test {
		int LOGIN_REQ = 1000;

		int ONLINE_LOGIN_REQ = 1001;
		int PLAYER_INDEX_REQ = 1002;
		int ROOM_LOGIN_REQ = 1003;

		int WS_PB_LOGIN_REQ = 1006;


		int ONLINE_LOGIN_RESP = 1000000;
		int PLAYER_INDEX_RESP = 1000001;

		int ROOM_LOGIN_RESP = 1003000;

		int HTTP_LOGIN_RESP = 2000000;
	}
}
