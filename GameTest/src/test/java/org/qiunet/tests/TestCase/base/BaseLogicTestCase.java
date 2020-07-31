package org.qiunet.tests.TestCase.base;

import com.google.protobuf.GeneratedMessageV3;
import org.qiunet.test.server.IServer;
import org.qiunet.test.testcase.http.HttpProtobufTestCase;
import org.qiunet.tests.proto.HeaderProto;
import org.qiunet.tests.robot.Robot;
import org.qiunet.tests.server.type.ServerType;

/**
 * Created by qiunet.
 * 17/12/9
 */
public abstract class BaseLogicTestCase<RequestData extends GeneratedMessageV3, ResponseData extends GeneratedMessageV3> extends HttpProtobufTestCase<RequestData, ResponseData, Robot> {

	protected HeaderProto.RequestHeader headerBuilder(Robot robot) {
		return HeaderProto.RequestHeader.newBuilder()
				.setUid(robot.getUid())
				.setToken(robot.getToken())
				.build();
	}

	@Override
	protected IServer getServer() {
		return ServerType.HTTP_LOGIC;
	}
}
