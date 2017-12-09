package org.qiunet.test.TestCase;

import com.google.protobuf.GeneratedMessageV3;
import org.qiunet.test.robot.Robot;
import org.qiunet.test.server.ServerType;
import org.qiunet.test.testcase.http.HttpProtobufTestCase;

import java.net.URI;

/**
 * Created by qiunet.
 * 17/12/9
 */
public abstract class BaseLogicTestCase<RequestData extends GeneratedMessageV3, ResponseData extends GeneratedMessageV3> extends HttpProtobufTestCase<RequestData, ResponseData, Robot> {
	@Override
	public URI getServerUri() {
		return ServerType.HTTP_LOGIC.uri();
	}
}
