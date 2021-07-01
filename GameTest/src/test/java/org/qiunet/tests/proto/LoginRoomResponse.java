package org.qiunet.tests.proto;

import org.qiunet.flash.handler.context.request.data.pb.IpbChannelData;
import org.qiunet.flash.handler.context.request.data.pb.PbChannelData;

import static org.qiunet.tests.protocol.ProtocolId.Test.ROOM_LOGIN_RESP;

/***
 *
 *
 * @author qiunet
 * 2020-09-22 21:00
 */
@PbChannelData(ID = ROOM_LOGIN_RESP, desc = "登录room响应")
public class LoginRoomResponse implements IpbChannelData {

	private int roomSize;

	public static LoginRoomResponse valueOf(int roomSize) {
		LoginRoomResponse response = new LoginRoomResponse();
		response.roomSize = roomSize;
		return response;
	}

	public int getRoomSize() {
		return roomSize;
	}
}
