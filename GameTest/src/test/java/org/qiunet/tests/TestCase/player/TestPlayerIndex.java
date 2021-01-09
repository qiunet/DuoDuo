package org.qiunet.tests.TestCase.player;

import org.qiunet.tests.TestCase.base.PersistOnlineTestCase;
import org.qiunet.tests.proto.PlayerIndexRequest;
import org.qiunet.tests.robot.Robot;

/**
 * Created by qiunet.
 * 17/12/9
 */
public class TestPlayerIndex extends PersistOnlineTestCase<PlayerIndexRequest> {
	@Override
	protected PlayerIndexRequest requestBuild(Robot robot) {
		return new PlayerIndexRequest();
	}

	@Override
	public boolean conditionJudge(Robot robot) {
		return true;
	}

	@Override
	protected int syncWaitForResponse() {
		return 1000001;
	}

	@Override
	public int getRequestID() {
		return 1002;
	}
}
