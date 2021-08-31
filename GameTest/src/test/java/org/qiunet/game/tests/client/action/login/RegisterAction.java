package org.qiunet.game.tests.client.action.login;

import org.qiunet.flash.handler.netty.server.param.adapter.message.StatusTipsResponse;
import org.qiunet.function.ai.enums.ActionStatus;
import org.qiunet.game.test.response.TestResponse;
import org.qiunet.game.test.robot.Robot;
import org.qiunet.game.tests.client.action.base.TestAction;
import org.qiunet.game.tests.client.anno.StatusTipsHandler;
import org.qiunet.game.tests.client.data.BlackBoard;
import org.qiunet.game.tests.client.data.condition.RandomNamePresent;
import org.qiunet.game.tests.client.data.condition.RegisterCountCondition;
import org.qiunet.game.tests.protocol.ProtocolId;
import org.qiunet.game.tests.protocol.enums.GenderType;
import org.qiunet.game.tests.protocol.proto.RegisterRequest;
import org.qiunet.game.tests.protocol.proto.RegisterResponse;
import org.qiunet.game.tests.server.enums.GameStatus;

/***
 * 注册新角色
 *
 * qiunet
 * 2021/8/5 10:55
 **/
public class RegisterAction extends TestAction {
	/**
	 * 是否错误
	 */
	private int errorMsg;

	@Override
	public void release() {
		super.release();
		this.errorMsg = 0;
	}

	public RegisterAction(Robot robot) {
		super(robot, new RandomNamePresent().and(new RegisterCountCondition(3)));
	}

	@Override
	public ActionStatus execute() {
		this.sendMessage(RegisterRequest.valueOf(BlackBoard.randomName.get(robot), GenderType.MALE, 1));
		return ActionStatus.RUNNING;
	}

	@Override
	protected ActionStatus runningStatusUpdate() {
		if (errorMsg > 0) {
			return ActionStatus.FAILURE;
		}
		return robot.isAuth() ? ActionStatus.SUCCESS : ActionStatus.FAILURE;
	}

	@Override
	@StatusTipsHandler({GameStatus.RANDOM_NAME_ALREADY_USED, GameStatus.REGISTER_COUNT_MAX})
	public void statusHandler(StatusTipsResponse response) {
		this.errorMsg = response.getStatus();
	}

	@TestResponse(ProtocolId.Login.REGISTER_RSP)
	public void registerResp(RegisterResponse response) {
		BlackBoard.loginInfo.get(robot).add(response.getLoginInfo());
		robot.setId(response.getLoginInfo().getPlayerId());
	}
}
