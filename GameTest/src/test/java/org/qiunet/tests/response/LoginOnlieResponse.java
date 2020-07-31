package org.qiunet.tests.response;

import org.qiunet.test.response.ProtobufResponse;
import org.qiunet.test.response.annotation.Response;
import org.qiunet.tests.proto.LoginOnlineProto;
import org.qiunet.tests.robot.Robot;

/**
 * Created by qiunet.
 * 17/12/9
 */
@Response(ID = 1000000)
public class LoginOnlieResponse extends ProtobufResponse<LoginOnlineProto.LoginOnlineResponse, Robot> {

	@Override
	public void response(Robot robot, LoginOnlineProto.LoginOnlineResponse loginOnlineResponse) {
		robot.counterDays = loginOnlineResponse.getDay();
	}
}
