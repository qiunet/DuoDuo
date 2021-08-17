package org.qiunet.game.tests.client.action.login;

import org.qiunet.function.condition.Conditions;
import org.qiunet.game.test.behavior.enums.ActionStatus;
import org.qiunet.game.test.response.TestResponse;
import org.qiunet.game.tests.client.action.base.TestAction;
import org.qiunet.game.tests.client.data.BlackBoard;
import org.qiunet.game.tests.client.data.condition.RandomNamePresent;
import org.qiunet.game.tests.client.data.condition.RegisterCountCondition;
import org.qiunet.game.tests.protocol.ProtocolId;
import org.qiunet.game.tests.protocol.enums.GenderType;
import org.qiunet.game.tests.protocol.proto.RegisterRequest;
import org.qiunet.game.tests.protocol.proto.RegisterResponse;

/***
 * 注册新角色
 *
 * qiunet
 * 2021/8/5 10:55
 **/
public class RegisterAction extends TestAction {

	public RegisterAction() {
		super(new Conditions<>(new RandomNamePresent(), new RegisterCountCondition(3)));
	}

	@Override
	public ActionStatus execute() {
		this.sendMessage(RegisterRequest.valueOf(BlackBoard.randomName.get(robot), GenderType.MALE, 1));
		return ActionStatus.SUCCESS;
	}

	@TestResponse(ProtocolId.Login.REGISTER_RSP)
	public void registerResp(RegisterResponse response) {
		BlackBoard.loginInfo.get(robot).add(response.getLoginInfo());
		robot.setId(response.getLoginInfo().getPlayerId());
	}
}
