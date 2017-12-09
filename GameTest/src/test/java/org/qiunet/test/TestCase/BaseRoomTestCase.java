package org.qiunet.test.TestCase;

import com.google.protobuf.GeneratedMessageV3;
import org.qiunet.test.robot.Robot;
import org.qiunet.test.server.IServer;
import org.qiunet.test.server.ServerType;
import org.qiunet.test.testcase.LongConn.LongConnProtobufTestCase;

/**
 * Created by qiunet.
 * 17/12/9
 */
public abstract class BaseRoomTestCase<ResponseData extends GeneratedMessageV3> extends LongConnProtobufTestCase<ResponseData , Robot> {

	@Override
	protected IServer getServer() {
		return ServerType.LC_ROOM;
	}
}
