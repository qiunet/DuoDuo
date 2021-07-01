package org.qiunet.tests.response;

import org.qiunet.test.response.ProtobufResponse;
import org.qiunet.test.response.annotation.Response;
import org.qiunet.tests.proto.LoginOnlineResponse;
import org.qiunet.tests.protocol.ProtocolId;
import org.qiunet.tests.robot.Robot;

/**
 * Created by qiunet.
 * 17/12/9
 */
@Response(ID = ProtocolId.Test.ONLINE_LOGIN_RESP)
public class LoginOnlieResp extends ProtobufResponse<LoginOnlineResponse, Robot> {

	@Override
	public void response(Robot robot, LoginOnlineResponse loginOnlineResponse) {
		robot.counterDays = loginOnlineResponse.getDay();
	}
}
