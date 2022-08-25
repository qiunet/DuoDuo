package org.qiunet.game.tests.client.action.login;

import org.qiunet.function.ai.enums.ActionStatus;
import org.qiunet.function.ai.node.action.BehaviorAction;
import org.qiunet.function.condition.IConditions;
import org.qiunet.game.test.response.TestResponse;
import org.qiunet.game.test.robot.Robot;
import org.qiunet.game.tests.client.action.base.TestAction;
import org.qiunet.game.tests.client.data.BlackBoard;
import org.qiunet.game.tests.protocol.proto.login.LoginRequest;
import org.qiunet.game.tests.protocol.proto.login.LoginResponse;

/***
 * 登录行为构造
 *
 * qiunet
 * 2021/7/26 17:23
 **/
@BehaviorAction(name = "登录")
public class LoginAction extends TestAction {

	public LoginAction(IConditions<Robot> conditions) {
		super(conditions);
	}

	@Override
	public ActionStatus execute() {
		LoginRequest loginRequest = LoginRequest.valueOf(getOwner().getAccount());
		this.sendMessage(loginRequest);
		return ActionStatus.RUNNING;
	}

	@Override
	protected ActionStatus runningStatusUpdate() {
		return 	BlackBoard.loginInfo.isNull(getOwner()) ? ActionStatus.RUNNING : ActionStatus.SUCCESS;
	}

	/**
	 * 登录的响应.
	 * @param loginResponse 登录下行数据
	 */
	@TestResponse
	public void loginResponse(LoginResponse loginResponse) {
		BlackBoard.loginInfo.set(getOwner(), loginResponse.getInfos());
	}
}
