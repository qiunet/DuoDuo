package org.qiunet.test.TestCase.base;

import com.google.protobuf.GeneratedMessageV3;
import org.qiunet.test.proto.HeaderProto;
import org.qiunet.test.robot.Robot;
import org.qiunet.test.server.IServer;
import org.qiunet.test.server.type.ServerType;
import org.qiunet.test.testcase.LongConn.LongConnProtobufTestCase;

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
	protected IServer getServer() {
		return ServerType.LC_ROOM;
	}
}
