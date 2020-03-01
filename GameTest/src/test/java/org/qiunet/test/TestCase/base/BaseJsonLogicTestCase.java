package org.qiunet.test.TestCase.base;

import org.qiunet.test.robot.Robot;
import org.qiunet.test.server.IServer;
import org.qiunet.test.server.type.ServerType;
import org.qiunet.test.testcase.http.HttpJsonTestCase;

/**
 * Created by qiunet.
 * 18/1/30
 */
public abstract class BaseJsonLogicTestCase extends HttpJsonTestCase<Robot> {

	@Override
	protected IServer getServer() {
		return ServerType.HTTP_LOGIC;
	}
}
