package org.qiunet.tests.TestCase.base;

import com.google.protobuf.GeneratedMessageV3;
import org.qiunet.test.testcase.LongConn.LongConnProtobufTestCase;
import org.qiunet.tests.proto.HeaderProto;
import org.qiunet.tests.robot.Robot;
import org.qiunet.tests.server.type.ServerType;

/**
 * Created by qiunet.
 * 17/12/9
 */
public abstract class BaseRoomTestCase<RequestData extends GeneratedMessageV3> extends LongConnProtobufTestCase<RequestData , Robot> {

	protected HeaderProto.RequestHeader headerBuilder(Robot robot) {
		return HeaderProto.RequestHeader.newBuilder()
				.setUid(robot.getUid())
				.setToken(robot.getToken())
				.build();
	}

	@Override
	protected ServerType getServer() {
		return ServerType.LC_ROOM;
	}
}
