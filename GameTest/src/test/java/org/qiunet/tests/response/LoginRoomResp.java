package org.qiunet.tests.response;

import org.qiunet.test.response.ProtobufResponse;
import org.qiunet.test.response.annotation.Response;
import org.qiunet.tests.proto.LoginRoomResponse;
import org.qiunet.tests.robot.Robot;

/**
 * Created by qiunet.
 * 17/12/11
 */
@Response(ID = 1000003)
public class LoginRoomResp extends ProtobufResponse<LoginRoomResponse, Robot> {

	@Override
	public void response(Robot robot, LoginRoomResponse loginRoomResponse) {
		robot.roomSize = loginRoomResponse.getRoomSize();
	}
}
