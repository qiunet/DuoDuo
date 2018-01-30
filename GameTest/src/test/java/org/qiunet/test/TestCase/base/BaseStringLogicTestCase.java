package org.qiunet.test.TestCase.base;

import org.qiunet.test.robot.Robot;
import org.qiunet.test.server.IServer;
import org.qiunet.test.server.type.ServerType;
import org.qiunet.test.testcase.http.HttpStringTestCase;

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
