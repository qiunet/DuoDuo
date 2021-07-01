package org.qiunet.tests.TestCase.base;

import org.qiunet.flash.handler.context.request.data.pb.IpbChannelData;
import org.qiunet.test.testcase.persistconn.PersistConnProtobufTestCase;
import org.qiunet.tests.robot.Robot;
import org.qiunet.tests.server.type.ServerType;

/**
 * Created by qiunet.
 * 17/12/9
 */
public abstract class PersistRoomTestCase<RequestData extends IpbChannelData> extends PersistConnProtobufTestCase<RequestData , Robot> {

	@Override
	protected ServerType getServer() {
		return ServerType.LC_ROOM;
	}
}
