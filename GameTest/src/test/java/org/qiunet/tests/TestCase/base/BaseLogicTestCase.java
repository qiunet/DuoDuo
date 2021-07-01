package org.qiunet.tests.TestCase.base;

import org.qiunet.flash.handler.context.request.data.pb.IpbChannelData;
import org.qiunet.test.server.IServer;
import org.qiunet.test.testcase.http.HttpProtobufTestCase;
import org.qiunet.tests.robot.Robot;
import org.qiunet.tests.server.type.ServerType;

/**
 * Created by qiunet.
 * 17/12/9
 */
public abstract class BaseLogicTestCase<RequestData extends IpbChannelData, ResponseData extends IpbChannelData> extends HttpProtobufTestCase<RequestData, ResponseData, Robot> {

	@Override
	protected IServer getServer() {
		return ServerType.HTTP_LOGIC;
	}
}
