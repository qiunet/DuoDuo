package org.qiunet.tests.proto;

import com.baidu.bjf.remoting.protobuf.annotation.ProtobufClass;
import org.qiunet.flash.handler.context.request.data.pb.IpbResponseData;
import org.qiunet.flash.handler.context.request.data.pb.PbResponse;

/***
 *
 *
 * @author qiunet
 * 2020-09-22 21:00
 */
@ProtobufClass
@PbResponse(ID = 1000003)
public class LoginRoomResponse implements IpbResponseData {

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
