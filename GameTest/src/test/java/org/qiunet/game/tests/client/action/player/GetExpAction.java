package org.qiunet.game.tests.client.action.player;

import org.qiunet.function.ai.enums.ActionStatus;
import org.qiunet.function.ai.node.action.BehaviorAction;
import org.qiunet.function.condition.IConditions;
import org.qiunet.game.test.response.TestResponse;
import org.qiunet.game.test.robot.Robot;
import org.qiunet.game.tests.client.action.base.TestAction;
import org.qiunet.game.tests.client.data.BlackBoard;
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
@BehaviorAction(name = "获得经验")
public class GetExpAction extends TestAction {

	private boolean getResp;

	@Override
	public void release() {
		super.release();
		this.getResp = false;
	}

	public GetExpAction(IConditions<Robot> conditions) {
		super(conditions);
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

	@TestResponse
	public void resp(GetExpResponse response) {
		this.getResp = true;
	}

	@TestResponse
	public void expChange(ExpChangePush push) {
		PlayerData data = BlackBoard.playerData.get(getOwner());
		data.setExp(push.getExp());
		data.setLv(push.getLevel());
	}
}
