package org.qiunet.tests.TestCase.base;

import org.qiunet.flash.handler.context.request.data.pb.IpbRequestData;
import org.qiunet.test.testcase.LongConn.LongConnProtobufTestCase;
import org.qiunet.tests.robot.Robot;
import org.qiunet.tests.server.type.ServerType;

/**
 * Created by qiunet.
 * 17/12/9
 */
public abstract class BaseOnlineTestCase<RequestData extends IpbRequestData> extends LongConnProtobufTestCase<RequestData, Robot> {


	@Override
	protected ServerType getServer() {
		return ServerType.LC_ONLINE;
	}
}
