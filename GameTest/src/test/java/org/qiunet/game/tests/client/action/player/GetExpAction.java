package org.qiunet.game.tests.client.action.player;

import org.qiunet.function.ai.enums.ActionStatus;
import org.qiunet.game.test.response.TestResponse;
import org.qiunet.game.test.robot.Robot;
import org.qiunet.game.tests.client.action.base.TestAction;
import org.qiunet.game.tests.client.data.BlackBoard;
import org.qiunet.game.tests.client.data.condition.ExpEnoughCondition;
import org.qiunet.game.tests.protocol.ProtocolId;
import org.qiunet.game.tests.protocol.proto.login.PlayerData;
import org.qiunet.game.tests.protocol.proto.player.ExpChangePush;
import org.qiunet.game.tests.protocol.proto.player.GetExpRequest;
import org.qiunet.game.tests.protocol.proto.player.GetExpResponse;

/***
 *
 *
 * qiunet
 * 2021/9/2 11:32
 **/
public class GetExpAction extends TestAction {

	private boolean getResp;

	@Override
	public void release() {
		super.release();
		this.getResp = false;
	}

	public GetExpAction(Robot robot) {
		super(robot, new ExpEnoughCondition().not());
	}

	@Override
	protected ActionStatus execute() {
		this.sendMessage(GetExpRequest.valueOf());
		return ActionStatus.RUNNING;
	}

	@Override
	protected ActionStatus runningStatusUpdate() {
		return this.getResp ? ActionStatus.SUCCESS : ActionStatus.RUNNING;
	}

	@TestResponse(ProtocolId.Player.GET_EXP_RSP)
	public void resp(GetExpResponse response) {
		this.getResp = true;
	}

	@TestResponse(ProtocolId.Player.EXP_CHANGE_PUSH)
	public void expChange(ExpChangePush push) {
		PlayerData data = BlackBoard.playerData.get(robot);
		data.setExp(push.getExp());
		data.setLv(push.getLevel());
	}
}