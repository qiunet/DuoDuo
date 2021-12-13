package org.qiunet.game.tests.client.action.player;

import org.qiunet.function.ai.enums.ActionStatus;
import org.qiunet.function.ai.node.action.BehaviorAction;
import org.qiunet.function.condition.IConditions;
import org.qiunet.game.test.response.TestResponse;
import org.qiunet.game.test.robot.Robot;
import org.qiunet.game.tests.client.action.base.TestAction;
import org.qiunet.game.tests.client.data.condition.ExpEnoughCondition;
import org.qiunet.game.tests.protocol.ProtocolId;
import org.qiunet.game.tests.protocol.proto.player.UpgradeLevelRequest;
import org.qiunet.game.tests.protocol.proto.player.UpgradeLevelResponse;

/***
 *
 *
 * qiunet
 * 2021/9/2 12:10
 **/
@BehaviorAction(desc = "升级")
public class UpgradeLvAction extends TestAction {
	private boolean resp;

	@Override
	public void release() {
		super.release();
		this.resp = false;
	}

	public UpgradeLvAction(Robot robot) {
		this(robot, new ExpEnoughCondition());
	}

	public UpgradeLvAction(Robot robot, IConditions<Robot> conditions) {
		super(robot, conditions);
	}

	@Override
	protected ActionStatus execute() {
		this.sendMessage(UpgradeLevelRequest.valueOf());
		return ActionStatus.RUNNING;
	}

	@Override
	protected ActionStatus runningStatusUpdate() {
		return resp ? ActionStatus.SUCCESS : ActionStatus.RUNNING;
	}

	@TestResponse(ProtocolId.Player.UPGRADE_LV_RSP)
	public void resp(UpgradeLevelResponse response) {
		this.resp = true;
	}
}
