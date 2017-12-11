package org.qiunet.test.TestCase.player;

import org.qiunet.test.TestCase.base.BaseOnlineTestCase;
import org.qiunet.test.proto.PlayerIndexProto;
import org.qiunet.test.robot.Robot;

/**
 * Created by qiunet.
 * 17/12/9
 */
public class TestPlayerIndex extends BaseOnlineTestCase<PlayerIndexProto.PlayerIndexRequest> {
	@Override
	protected PlayerIndexProto.PlayerIndexRequest requestBuild(Robot robot) {
		return PlayerIndexProto.PlayerIndexRequest.newBuilder().setHeader(headerBuilder(robot)).build();
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
