package org.qiunet.cross.test.handler;

import org.qiunet.flash.handler.common.id.IProtocolId;

/***
 *
 *
 * @author qiunet
 * 2020-10-23 09:52
 */
public final class ProtocolId implements IProtocolId {

	public interface Player {
		int PLAYER_LOGIN = 1000;

		int CROSS_PLAYER_LOGIN_SUCCESS = 1000001;
	}
}
