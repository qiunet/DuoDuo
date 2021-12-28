package org.qiunet.game.tests.client.action.login;

import org.qiunet.flash.handler.netty.server.param.adapter.message.StatusTipsRsp;
import org.qiunet.function.ai.enums.ActionStatus;
import org.qiunet.function.ai.node.action.BehaviorAction;
import org.qiunet.function.condition.IConditions;
import org.qiunet.game.test.response.TestResponse;
import org.qiunet.game.test.robot.Robot;
import org.qiunet.game.tests.client.action.base.TestAction;
import org.qiunet.game.tests.client.anno.StatusTipsHandler;
import org.qiunet.game.tests.client.data.BlackBoard;
import org.qiunet.game.tests.protocol.ProtocolId;
import org.qiunet.game.tests.protocol.enums.GenderType;
import org.qiunet.game.tests.protocol.proto.login.RegisterRequest;
import org.qiunet.game.tests.protocol.proto.login.RegisterResponse;
import org.qiunet.game.tests.server.enums.GameStatus;

/***
 * 注册新角色
 *
 * qiunet
 * 2021/8/5 10:55
 **/
@BehaviorAction(name = "注册新角色")
public class RegisterAction extends TestAction {
	/**
	 * 是否错误
	 */
	private int errorMsg;
	/**
	 * 已经完成注册
	 */
	private boolean registered;

	@Override
	public void release() {
		super.release();
		this.errorMsg = 0;
		this.registered = false;
	}

	public RegisterAction(IConditions<Robot> conditions) {
		super(conditions);
	}

	@Override
	public ActionStatus execute() {
		this.sendMessage(RegisterRequest.valueOf(BlackBoard.randomName.get(getOwner()), GenderType.MALE, 1));
		return ActionStatus.RUNNING;
	}

	@Override
	protected ActionStatus runningStatusUpdate() {
		if (errorMsg > 0) {
			return ActionStatus.FAILURE;
		}
		return registered ? ActionStatus.SUCCESS : ActionStatus.RUNNING;
	}

	@Override
	@StatusTipsHandler({GameStatus.RANDOM_NAME_ALREADY_USED, GameStatus.REGISTER_COUNT_MAX})
	public void statusHandler(StatusTipsRsp response) {
		this.errorMsg = response.getStatus();
	}

	@TestResponse(ProtocolId.Login.REGISTER_RSP)
	public void registerResp(RegisterResponse response) {
		BlackBoard.loginInfo.get(getOwner()).add(response.getLoginInfo());
		this.registered = true;
	}
}
