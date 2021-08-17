package org.qiunet.game.tests.client.action.login;

import org.qiunet.function.condition.ConditionsNot;
import org.qiunet.game.test.behavior.enums.ActionStatus;
import org.qiunet.game.test.response.TestResponse;
import org.qiunet.game.tests.client.action.base.TestAction;
import org.qiunet.game.tests.client.data.BlackBoard;
import org.qiunet.game.tests.client.data.condition.LoginCondition;
import org.qiunet.game.tests.protocol.ProtocolId;
import org.qiunet.game.tests.protocol.proto.LoginRequest;
import org.qiunet.game.tests.protocol.proto.LoginResponse;

/***
 * 登录行为构造
 *
 * qiunet
 * 2021/7/26 17:23
 **/
public class LoginAction extends TestAction {

	public LoginAction() {
		super(new ConditionsNot<>(new LoginCondition()));
	}

	@Override
	public ActionStatus execute() {
		LoginRequest loginRequest = LoginRequest.valueOf(robot.getAccount());
		this.sendMessage(loginRequest);
		return ActionStatus.SUCCESS;
	}

	/**
	 * 登录的响应.
	 * @param loginResponse 登录下行数据
	 */
	@TestResponse(ProtocolId.Login.LOGIN_RSP)
	public void loginResponse(LoginResponse loginResponse) {
		BlackBoard.loginInfo.set(robot, loginResponse.getInfos());
	}
}
