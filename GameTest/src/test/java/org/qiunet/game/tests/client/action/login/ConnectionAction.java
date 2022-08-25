package org.qiunet.game.tests.client.action.login;

import org.qiunet.flash.handler.netty.server.message.ConnectionReq;
import org.qiunet.flash.handler.netty.server.message.ConnectionRsp;
import org.qiunet.function.ai.enums.ActionStatus;
import org.qiunet.function.ai.node.action.BehaviorAction;
import org.qiunet.function.condition.IConditions;
import org.qiunet.game.test.response.TestResponse;
import org.qiunet.game.test.robot.Robot;
import org.qiunet.game.tests.client.action.base.TestAction;
import org.qiunet.game.tests.client.data.BlackBoard;
import org.qiunet.game.tests.server.enums.ServerType;

/***
 * 登录行为构造
 *
 * qiunet
 * 2021/7/26 17:23
 **/
@BehaviorAction(name = "连接协议")
public class ConnectionAction extends TestAction {

	public ConnectionAction(IConditions<Robot> conditions) {
		super(conditions);
	}

	@Override
	public ActionStatus execute() {

		this.connector(ServerType.LC_ROOM.getClientConfig());

		ConnectionReq connectionReq = new ConnectionReq();
		connectionReq.setIdKey(getOwner().getAccount());
		this.sendMessage(connectionReq, true);
		return ActionStatus.RUNNING;
	}

	@Override
	protected ActionStatus runningStatusUpdate() {
		return BlackBoard.connected.get(getOwner()) ? ActionStatus.SUCCESS : ActionStatus.RUNNING;
	}

	/**
	 * 连接成功响应
	 * @param rsp 响应
	 */
	@TestResponse
	public void connectionRsp(ConnectionRsp rsp) {
		BlackBoard.connected.set(getOwner(), true);
	}
}
