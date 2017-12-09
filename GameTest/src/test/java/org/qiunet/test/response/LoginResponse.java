package org.qiunet.test.response;

import org.qiunet.test.proto.LoginProto;
import org.qiunet.test.response.annotation.Response;
import org.qiunet.test.robot.Robot;

/**
 * Created by qiunet.
 * 17/12/9
 */
@Response(ID = 1000000)
public class LoginResponse extends ProtobufResponse<LoginProto.LoginResponse, Robot> {

	@Override
	public void response(Robot robot, LoginProto.LoginResponse loginResponse) {
		robot.setUidAndToken(loginResponse.getUid(), loginResponse.getToken());
	}
}
