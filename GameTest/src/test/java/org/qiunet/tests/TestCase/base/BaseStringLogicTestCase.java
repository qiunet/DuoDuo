package org.qiunet.tests.TestCase.base;

import org.qiunet.test.server.IServer;
import org.qiunet.test.testcase.http.HttpStringTestCase;
import org.qiunet.tests.robot.Robot;
import org.qiunet.tests.server.type.ServerType;

/**
 * Created by qiunet.
 * 18/1/30
 */
public abstract class BaseStringLogicTestCase extends HttpStringTestCase<Robot> {

	@Override
	protected IServer getServer() {
		return ServerType.HTTP_LOGIC;
	}
}
